# projet-synthese-ing3
<h1> SCOPE SÉCURITÉ : CHIFFRER LES DONNÉES SENSIBLES AU CŒUR DU LOGICIEL </h1>


<h2>INTRODUCTION</h2>


La contrainte technique impose de différencier ce qui est considéré comme une donnée sensible de ce qui ne l'est pas. L'objectif final n'étant pas de tout chiffrer. 

Dans le cadre de notre UC, nous avons considéré comme donnée sensible toute donnée à caractère personnel (par exemple les données de géolocalisation, et les données biométriques) ainsi que toute donnée faisant référence à des identifiants d'accès (C'est-à-dire tout couple de login/mot de passe, token d'accès pour la base de données ou autre platforme. Typiquement stockés dans les fichiers properties de l'application)

L'architecture finale permettant de chiffrer/déchiffrer des données implique trois composants principaux que sont : Hashicorp Vault pour le stockage des clés, une librairie de cryptographie exposant une implémentation d'un algorithme de chiffrement, ainsi qu'un Key Management System (API) permettant de faire interface entre Vault et nos autres microservices.
Ce document explique nos choix d'architecture ainsi que nos stratégies en matière de chiffrement et gestion de clés.


<h2>I - LIBRAIRIE DE CRYPTOGRAPHIE (CRYPTOLIB)</h2>

La libraire de cryptographie expose une implémentation de l'algorithme Advanced
Encryption Standard (AES) utilisant le mode Galois/Counter Mode (GCM).
Ce mode de chiffrement a été choisi pour sa performance (Chiffrement de blocs en parallèle) ainsi que pour son authentification intégrée grace au calcul d'un tag (authentication tag).
Les spécifications de l'algorithme sont :
- Une clé de 256 bits
- Un nonce de 96 bits
- Un tag d'authentification de 128 bits

<h2>II - HASHICORP VAULT</h2>

Plusieurs problématiques se posent quant à la gestion et stockage des clés de chiffrement, à savoir :

 - Comment les stocker de manière sécurisée ? 
 -Comment gérer leur cycle de vie ?
 
Après plusieurs recherches, il semblerait qu' Hashicorp Vault réponde à ces deux problématiques. Il rend possible le stockage sécurisé de secrets, clés etc. et expose également une fonctionnalité Encryption as a Service (EaaS). Dans le cadre de notre scope, nous avons créé une instance de Vault par environnement (Dev, Staging, Prod).

<h3>Gestions de clés : </h3>

Les recommandations en matière de sécurité énoncent que la durée de vie d'une clé de chiffrement doit être limitée dans le temps. Il est donc conseillé d'effectuer une rotation de clé de chiffrement régulièrement. Traditionnellement, cela requiert de déchiffrer l'ensemble des données puis générer une nouvelle clé et chiffrer à nouveau les données avec la nouvelle clé :
Or le chiffrement est une opération couteuse en terme de calcul et de temps, surtout si le volume de données est important. Pour cela, nous avons choisi d'utiliser le chiffrement sur deux niveaux :
La Key Encryption Key (KEK) est une la clé qui va chiffrer la Data Encryption Key (DEK) La Data Encryption Key (DEK) est la clé qui va chiffrer les données

<h2> III - KEY MANAGEMENT SYSTEM (API) </h2>

C'est l'API permettant de faire interface avec Vault. Elle permet de gérer le cycle de vie des clés en assurant leurs rotations. Elle expose deux endpoints :

 - GET /keys/dek : Renvoie la Data Encryption Key
C'est l'endpoint que chaque microservice doit appeler pour obtenir la Data Encryption Key
avant de chiffrer des données.

- POST /keys/rotate Déclenche le mécanisme de rotation de clés
C'est le endpoint qui va etre appelé de manière périodique (toutes les semaines), pour effectuer une rotation de la clef (la KEK) au niveau du VAULT
  

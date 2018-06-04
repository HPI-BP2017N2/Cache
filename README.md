# Cache [![Build Status](https://travis-ci.org/HPI-BP2017N2/Cache.svg?branch=master)](https://travis-ci.org/HPI-BP2017N2/Cache)
The cache is a microservice component, which downloads, stores and provides idealo's offers of specific shops.
[The matcher](https://github.com/HPI-BP2017N2/Matcher) uses the information for finding counterparts for parsed offers, [the model generator](https://github.com/HPI-BP2017N2/MachineLearningModelGenerator) uses the information for model generation.
The microservice is written in Java and uses the Spring framework.

## Getting started
### Prerequisites

To run the microservice it is required to set up the following:

1. MongoDB  
 The MongoDB is used to store the offers. Every shop will have and own collection named by shop ID.

2. Idealo bridge  
 The cache downloads the offers from the bridge.

3. [URL-Cleaner microservice](https://github.com/HPI-BP2017N2/URLCleaner)  
 Since the urls of the idealo offers can contain click trackers, it is required to clean them before storing, since the URL is one feature for matching.
 
#### Environment variables
- CACHE_PORT: The port that should be used by the cache
- API_URL: The root url of the idealo API
- ACCESS_TOKEN_URI: The uri which will be used to get an authorization token for the idealo bridge
- CLIENT_ID: The OAuth2 client ID for accessing the idealo bridge
- CLIENT_SECRET: The OAuth2 client secret ID for accessing the idealo bridge
- MONGO_IP: The IP of the MongoDB instance
- MONGO_PORT: The port of the MongoDB instance
- MONGO_CACHE_USER: The username to access the MongoDB
- MONGO_CACHE_PW: The password to access the MongoDB
- URLCLEANER_IP: The URI of the URL-Cleaner

#### Component properties
- categoryMappingLevel: Depth of the parent category of an idealo offer that should be stored.

## How it works
1. The cache receives a request to download the offers of one shop. If already downloading, the following steps will be skipped.  
2. The cache downloads all offers of the shop from the idealo bridge.  
3. The cache finds the indices of unique parts in the image URLs.  
4. For every offer, the cache  
 4.1. requests the cleaned URL from the URL cleaner,  
 4.2. finds a parent category on the specified level and  
 4.3. stores the offer.

### Further requests
- an offer of a shop in a specific phase
- an offer of a shop in a specific phase that has not already been matched
- an offer of a shop with by offer key
- mark an offer as matched
- update the phase of all offers to a specified new one
- delete all offers of one shop
  

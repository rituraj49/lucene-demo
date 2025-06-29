1. /locations/search API
     Request Body : New York


   
2. /locations/amadeus-search API
    Request Body :
{
  "keyword": "BOM",
  "subType": "AIRPORT"
                      }

   or
   {
  "keyword": "HYD",
  "subType": "CITY"
  }


3. /flights/search API GetAmmping 
     Request Body:
{
"originLocationCode":"SYD",
"destinationLocationCode":"NYC",
"departureDate":"2025-07-12",
"maxPrice":140000,
"adults":2,
"currencyCode":"INR"
}


4. /flights/search API PostMapping
   Reuest Body:
   {
  "currencyCode": "INR",
  "tripDetails": [
    {
      "id": "1",
      "from": "BOM",
      "to": "BLR",
      "departureDate": "2025-07-29",
      "departureTime": "10:00:00"
    },
    {
      "id": "2",
      "from": "BLR",
      "to": "HYD",
      "departureDate": "2025-07-30",
      "departureTime": "10:00:00"
    }
   
  ],
  "adults": 1,
  "children": 0,
  "infants": 0,
  "maxCount": 2,
  "cabin": "ECONOMY",
  "checkedBags": true,
  "refundableFare": true
}


5. /pricing/flights/confrim  API
   Request Body:
   [ {
            "type": "flight-offer",
            "id": "2",
            "source": "GDS",
            "instantTicketingRequired": false,
            "nonHomogeneous": false,
            "oneWay": false,
            "isUpsellOffer": false,
            "lastTicketingDate": "2025-06-30",
            "lastTicketingDateTime": "2025-06-30",
            "numberOfBookableSeats": 9,
            "itineraries": [
                {
                    "duration": "PT16H25M",
                    "segments": [
                        {
                            "departure": {
                                "iataCode": "SYD",
                                "terminal": "1",
                                "at": "2025-07-13T11:25:00"
                            },
                            "arrival": {
                                "iataCode": "XMN",
                                "terminal": "3",
                                "at": "2025-07-13T18:50:00"
                            },
                            "carrierCode": "MF",
                            "number": "802",
                            "aircraft": {
                                "code": "789"
                            },
                            "operating": {
                                "carrierCode": "MF"
                            },
                            "duration": "PT9H25M",
                            "id": "1",
                            "numberOfStops": 0,
                            "blacklistedInEU": false
                        },
                        {
                            "departure": {
                                "iataCode": "XMN",
                                "terminal": "3",
                                "at": "2025-07-13T22:20:00"
                            },
                            "arrival": {
                                "iataCode": "BKK",
                                "at": "2025-07-14T00:50:00"
                            },
                            "carrierCode": "MF",
                            "number": "843",
                            "aircraft": {
                                "code": "738"
                            },
                            "operating": {
                                "carrierCode": "MF"
                            },
                            "duration": "PT3H30M",
                            "id": "2",
                            "numberOfStops": 0,
                            "blacklistedInEU": false
                        }
                    ]
                },
                {
                    "duration": "PT18H5M",
                    "segments": [
                        {
                            "departure": {
                                "iataCode": "BKK",
                                "at": "2025-07-27T12:15:00"
                            },
                            "arrival": {
                                "iataCode": "XMN",
                                "terminal": "3",
                                "at": "2025-07-27T16:30:00"
                            },
                            "carrierCode": "MF",
                            "number": "854",
                            "aircraft": {
                                "code": "738"
                            },
                            "operating": {
                                "carrierCode": "MF"
                            },
                            "duration": "PT3H15M",
                            "id": "7",
                            "numberOfStops": 0,
                            "blacklistedInEU": false
                        },
                        {
                            "departure": {
                                "iataCode": "XMN",
                                "terminal": "3",
                                "at": "2025-07-27T22:00:00"
                            },
                            "arrival": {
                                "iataCode": "SYD",
                                "terminal": "1",
                                "at": "2025-07-28T09:20:00"
                            },
                            "carrierCode": "MF",
                            "number": "801",
                            "aircraft": {
                                "code": "789"
                            },
                            "operating": {
                                "carrierCode": "MF"
                            },
                            "duration": "PT9H20M",
                            "id": "8",
                            "numberOfStops": 0,
                            "blacklistedInEU": false
                        }
                    ]
                }
            ],
            "price": {
                "currency": "EUR",
                "total": "967.72",
                "base": "404.00",
                "fees": [
                    {
                        "amount": "0.00",
                        "type": "SUPPLIER"
                    },
                    {
                        "amount": "0.00",
                        "type": "TICKETING"
                    }
                ],
                "grandTotal": "967.72",
                "additionalServices": [
                    {
                        "amount": "339.48",
                        "type": "CHECKED_BAGS"
                    }
                ]
            },
            "pricingOptions": {
                "fareType": [
                    "PUBLISHED"
                ],
                "includedCheckedBagsOnly": true
            },
            "validatingAirlineCodes": [
                "MF"
            ],
            "travelerPricings": [
                {
                    "travelerId": "1",
                    "fareOption": "STANDARD",
                    "travelerType": "ADULT",
                    "price": {
                        "currency": "EUR",
                        "total": "483.86",
                        "base": "202.00"
                    },
                    "fareDetailsBySegment": [
                        {
                            "segmentId": "1",
                            "cabin": "ECONOMY",
                            "fareBasis": "S3M6AAUS",
                            "brandedFare": "YSTANDARD",
                            "brandedFareLabel": "ECONOMY STANDARD",
                            "class": "S",
                            "includedCheckedBags": {
                                "quantity": 1
                            },
                            "includedCabinBags": {
                                "quantity": 1
                            },
                            "amenities": [
                                {
                                    "description": "CHECKED BAG 1PC OF 23KG 158CM",
                                    "isChargeable": false,
                                    "amenityType": "BAGGAGE",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "PRE RESERVED SEAT ASSIGNMENT",
                                    "isChargeable": true,
                                    "amenityType": "PRE_RESERVED_SEAT",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "REFUNDABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "CHANGEABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                }
                            ]
                        },
                        {
                            "segmentId": "2",
                            "cabin": "ECONOMY",
                            "fareBasis": "S3M6AAUS",
                            "brandedFare": "YSTANDARD",
                            "brandedFareLabel": "ECONOMY STANDARD",
                            "class": "S",
                            "includedCheckedBags": {
                                "quantity": 1
                            },
                            "includedCabinBags": {
                                "quantity": 1
                            },
                            "amenities": [
                                {
                                    "description": "CHECKED BAG 1PC OF 23KG 158CM",
                                    "isChargeable": false,
                                    "amenityType": "BAGGAGE",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "PRE RESERVED SEAT ASSIGNMENT",
                                    "isChargeable": true,
                                    "amenityType": "PRE_RESERVED_SEAT",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "REFUNDABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "CHANGEABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                }
                            ]
                        },
                        {
                            "segmentId": "7",
                            "cabin": "ECONOMY",
                            "fareBasis": "S3M6AAUS",
                            "brandedFare": "YSTANDARD",
                            "brandedFareLabel": "ECONOMY STANDARD",
                            "class": "S",
                            "includedCheckedBags": {
                                "quantity": 1
                            },
                            "includedCabinBags": {
                                "quantity": 1
                            },
                            "amenities": [
                                {
                                    "description": "CHECKED BAG 1PC OF 23KG 158CM",
                                    "isChargeable": false,
                                    "amenityType": "BAGGAGE",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "PRE RESERVED SEAT ASSIGNMENT",
                                    "isChargeable": true,
                                    "amenityType": "PRE_RESERVED_SEAT",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "REFUNDABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "CHANGEABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                }
                            ]
                        },
                        {
                            "segmentId": "8",
                            "cabin": "ECONOMY",
                            "fareBasis": "S3M6AAUS",
                            "brandedFare": "YSTANDARD",
                            "brandedFareLabel": "ECONOMY STANDARD",
                            "class": "S",
                            "includedCheckedBags": {
                                "quantity": 1
                            },
                            "includedCabinBags": {
                                "quantity": 1
                            },
                            "amenities": [
                                {
                                    "description": "CHECKED BAG 1PC OF 23KG 158CM",
                                    "isChargeable": false,
                                    "amenityType": "BAGGAGE",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "PRE RESERVED SEAT ASSIGNMENT",
                                    "isChargeable": true,
                                    "amenityType": "PRE_RESERVED_SEAT",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "REFUNDABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "CHANGEABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                }
                            ]
                        }
                    ]
                },
                {
                    "travelerId": "2",
                    "fareOption": "STANDARD",
                    "travelerType": "ADULT",
                    "price": {
                        "currency": "EUR",
                        "total": "483.86",
                        "base": "202.00"
                    },
                    "fareDetailsBySegment": [
                        {
                            "segmentId": "1",
                            "cabin": "ECONOMY",
                            "fareBasis": "S3M6AAUS",
                            "brandedFare": "YSTANDARD",
                            "brandedFareLabel": "ECONOMY STANDARD",
                            "class": "S",
                            "includedCheckedBags": {
                                "quantity": 1
                            },
                            "includedCabinBags": {
                                "quantity": 1
                            },
                            "amenities": [
                                {
                                    "description": "CHECKED BAG 1PC OF 23KG 158CM",
                                    "isChargeable": false,
                                    "amenityType": "BAGGAGE",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "PRE RESERVED SEAT ASSIGNMENT",
                                    "isChargeable": true,
                                    "amenityType": "PRE_RESERVED_SEAT",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "REFUNDABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "CHANGEABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                }
                            ]
                        },
                        {
                            "segmentId": "2",
                            "cabin": "ECONOMY",
                            "fareBasis": "S3M6AAUS",
                            "brandedFare": "YSTANDARD",
                            "brandedFareLabel": "ECONOMY STANDARD",
                            "class": "S",
                            "includedCheckedBags": {
                                "quantity": 1
                            },
                            "includedCabinBags": {
                                "quantity": 1
                            },
                            "amenities": [
                                {
                                    "description": "CHECKED BAG 1PC OF 23KG 158CM",
                                    "isChargeable": false,
                                    "amenityType": "BAGGAGE",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "PRE RESERVED SEAT ASSIGNMENT",
                                    "isChargeable": true,
                                    "amenityType": "PRE_RESERVED_SEAT",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "REFUNDABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "CHANGEABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                }
                            ]
                        },
                        {
                            "segmentId": "7",
                            "cabin": "ECONOMY",
                            "fareBasis": "S3M6AAUS",
                            "brandedFare": "YSTANDARD",
                            "brandedFareLabel": "ECONOMY STANDARD",
                            "class": "S",
                            "includedCheckedBags": {
                                "quantity": 1
                            },
                            "includedCabinBags": {
                                "quantity": 1
                            },
                            "amenities": [
                                {
                                    "description": "CHECKED BAG 1PC OF 23KG 158CM",
                                    "isChargeable": false,
                                    "amenityType": "BAGGAGE",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "PRE RESERVED SEAT ASSIGNMENT",
                                    "isChargeable": true,
                                    "amenityType": "PRE_RESERVED_SEAT",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "REFUNDABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "CHANGEABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                }
                            ]
                        },
                        {
                            "segmentId": "8",
                            "cabin": "ECONOMY",
                            "fareBasis": "S3M6AAUS",
                            "brandedFare": "YSTANDARD",
                            "brandedFareLabel": "ECONOMY STANDARD",
                            "class": "S",
                            "includedCheckedBags": {
                                "quantity": 1
                            },
                            "includedCabinBags": {
                                "quantity": 1
                            },
                            "amenities": [
                                {
                                    "description": "CHECKED BAG 1PC OF 23KG 158CM",
                                    "isChargeable": false,
                                    "amenityType": "BAGGAGE",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "PRE RESERVED SEAT ASSIGNMENT",
                                    "isChargeable": true,
                                    "amenityType": "PRE_RESERVED_SEAT",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "REFUNDABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                },
                                {
                                    "description": "CHANGEABLE  TICKET",
                                    "isChargeable": true,
                                    "amenityType": "BRANDED_FARES",
                                    "amenityProvider": {
                                        "name": "BrandedFare"
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        }]
   
     

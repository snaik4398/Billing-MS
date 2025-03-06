FOR  text format pass 
  "format": "TXT"
For pdf format pass 
  "format": "PDF"
POST : localhost:8080/api/receipts/generate
{
  "items": [
    {
      "itemNumber": 1,
      "itemName": "Product A",
      "price": 10.99
    },
    {
      "itemNumber": 2,
      "itemName": "Product B",
      "price": 24.50
    }
  ],
  "company": {
    "name": "DNA Architect",
    "gstNumber": "GST1234567890",
    "logoPath": "static/logo.png"
  },
  "format": "TXT"
}


---------------------------------------------------------------

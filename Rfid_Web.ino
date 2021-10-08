/*PIN SDA/SS : D2
 *PIN SCK : D5
 *PIN MOSI: D7
 *PIN MISO: D6
 *PIN GND : GND
 *PIN RST : D1
 *PIN 3.3V: 3V
 *KAKI PANJANG LED HIJAU : D0
 *KAKI PANJANG LED MERAH : D4
 *SEMUA KAKI PENDEK LED : GND
 */
 
 
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h> 
#include <SPI.h>
#include <MFRC522.h>  
 
#define SS_PIN D2
#define RST_PIN D1
#define Merah D4
#define Hijau D0
 
#define WLAN_SSID "pw nya gampil"  //Nama Wi-Fi
#define WLAN_PASS "bismillah12345" //Password  
 
MFRC522 rfid(SS_PIN, RST_PIN); 
MFRC522::MIFARE_Key key;
 
String nama = "Danish"; //Diisi sesuai nama masing
String strID;
 
void setup() {
  Serial.begin(115200);
  pinMode(Merah, OUTPUT);
  pinMode(Hijau, OUTPUT);
  WiFi.begin(WLAN_SSID, WLAN_PASS);
 
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Menghubungkan");
  }
 
  if(WiFi.status() == WL_CONNECTED){
    Serial.println("Terhubung");
    }
  else{
    Serial.println("Gagal Terhubung");
  }
  SPI.begin(); // Init SPI bus
  rfid.PCD_Init(); // Init MFRC522 
}
 
void loop() {
  digitalWrite(Merah, LOW);
  digitalWrite(Hijau, LOW);
  if (!rfid.PICC_IsNewCardPresent() || !rfid.PICC_ReadCardSerial()) return;
  MFRC522::PICC_Type piccType = rfid.PICC_GetType(rfid.uid.sak);
  if (piccType != MFRC522::PICC_TYPE_MIFARE_MINI &&
      piccType != MFRC522::PICC_TYPE_MIFARE_1K &&
      piccType != MFRC522::PICC_TYPE_MIFARE_4K) {
    Serial.println(F("Your tag is not of type MIFARE Classic."));
    return;
  }
  strID = "";
  for (byte i = 0; i < 4; i++) {
    strID +=
      (rfid.uid.uidByte[i] < 0x10 ? "0" : "") +
      String(rfid.uid.uidByte[i], HEX) +
      (i != 3 ? ":" : "");
  }
  strID.toUpperCase();
  Serial.print("ID dari RFID Tag : ");
  Serial.println(strID);
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin("http://logrfid.sintara.co.id/data.php?rfid_tag="+String(strID)+"&nama="+String(nama)); //Alamat File Data.php Disimpan
    int httpCode = http.GET();
    if (httpCode > 0) {
      char json[500];
      String payload = http.getString();
      payload.toCharArray(json, 500);
 
      //StaticJsonDocument<200> doc;
      DynamicJsonDocument doc(JSON_OBJECT_SIZE(5));
 
     // Deserialize the JSON document
      deserializeJson(doc, json);
 
      String Pesan  = doc["Pesan"];
      if( Pesan == "1" ){
        Serial.println("Pesan : Kartu belum diaktifkan");
        digitalWrite(Merah, HIGH);
        delay(3000);
      } else if ( Pesan == "2" ){
        Serial.println("Pesan : Pengguna Tidak Aktif");
        digitalWrite(Merah, HIGH);
        delay(3000);
      } else if ( Pesan == "3" ){
        Serial.println("Pesan : Pengguna Aktif");
        digitalWrite(Hijau, HIGH);
        delay(3000);
      } else {
        Serial.println("Pesan : Pengguna Baru ditambahkan");
        digitalWrite(Hijau, HIGH);
        delay(3000);
      }
  }
    http.end();
  }
}

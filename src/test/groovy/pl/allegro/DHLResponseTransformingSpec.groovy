package pl.allegro

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static com.github.tomakehurst.wiremock.client.WireMock.*


class DHLResponseTransformingSpec extends Specification {


    WireMockServer server

    RestTemplate restTemplate

    def setup() {
        server = new WireMockServer(wireMockConfig().port(8080).extensions(DHLResponseTransformer))
        server.start()
        restTemplate = new RestTemplate()
    }

    def "test transformer"() {
        given:
            stubFor(
                    post(urlPathMatching("/dhl/GetShipments"))
                            .willReturn(aResponse().withStatus(200).withBody(EXAMPLE__RESPONSE).withTransformers("dhl-response-transformer")
                    )
            )

        when:
            def response = restTemplate.postForObject("http://localhost:8080/dhl/GetShipments", EXAMPLE_REQUEST, String)

        then:
            !response.empty


    }

    public static final String EXAMPLE_REQUEST = """
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://www.dhl.com.pl/tntwebservice/2014/04" xmlns:arr="http://schemas.microsoft.com/2003/10/Serialization/Arrays">
   <soapenv:Header/>
   <soapenv:Body>
      <ns:GetShipments>
         <ns:shipmentNumbers>
            <arr:string>16713587389</arr:string>
            <arr:string>26713587389</arr:string>
         </ns:shipmentNumbers>
      </ns:GetShipments>
   </soapenv:Body>
</soapenv:Envelope>
"""

    public static final String EXAMPLE__RESPONSE = """
<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
   <s:Body>
      <GetShipmentsResponse xmlns="http://www.dhl.com.pl/tntwebservice/2014/04">
         <GetShipmentsResult xmlns:a="http://schemas.datacontract.org/2004/07/com.dhl.pl.TnTWebService.v2.Interface" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
            <a:Shipment>
               <a:ClientReferenceNumber>SH00567626</a:ClientReferenceNumber>
               <a:Events>
                  <a:Event>
                     <a:Status>DWP</a:Status>
                     <a:Terminal>Warszawa</a:Terminal>
                     <a:Timestamp>2017-03-10T21:19:00</a:Timestamp>
                  </a:Event>
                  <a:Event>
                     <a:Status>SORT</a:Status>
                     <a:Terminal>Warszawa</a:Terminal>
                     <a:Timestamp>2017-03-12T21:19:12</a:Timestamp>
                  </a:Event>
                  <a:Event>
                     <a:Status>LK</a:Status>
                     <a:Terminal>Warszawa</a:Terminal>
                     <a:Timestamp>2017-03-13T07:20:41</a:Timestamp>
                  </a:Event>
                  <a:Event>
                     <a:Status>DOR</a:Status>
                     <a:Terminal>Warszawa</a:Terminal>
                     <a:Timestamp>2017-03-13T10:28:00</a:Timestamp>
                  </a:Event>
               </a:Events>
               <a:ReceiptDate>2017-03-13T10:28:00</a:ReceiptDate>
               <a:ReceivedBy>MAJEWSKI</a:ReceivedBy>
               <a:ReceivingTerminal>Warszawa</a:ReceivingTerminal>
               <a:SendingTerminal>Warszawa</a:SendingTerminal>
               <a:SentDate>2017-03-10T00:00:00</a:SentDate>
               <a:ShipmentNumber>16713587389</a:ShipmentNumber>
               <a:Status>FOUND</a:Status>
            </a:Shipment>
            <a:Shipment>
               <a:ClientReferenceNumber>SH00567626</a:ClientReferenceNumber>
               <a:Events>
                  <a:Event>
                     <a:Status>DWP</a:Status>
                     <a:Terminal>Warszawa</a:Terminal>
                     <a:Timestamp>2017-03-10T21:19:00</a:Timestamp>
                  </a:Event>
                  <a:Event>
                     <a:Status>SORT</a:Status>
                     <a:Terminal>Warszawa</a:Terminal>
                     <a:Timestamp>2017-03-12T21:19:12</a:Timestamp>
                  </a:Event>
                  <a:Event>
                     <a:Status>LK</a:Status>
                     <a:Terminal>Warszawa</a:Terminal>
                     <a:Timestamp>2017-03-13T07:20:41</a:Timestamp>
                  </a:Event>
                  <a:Event>
                     <a:Status>DOR</a:Status>
                     <a:Terminal>Warszawa</a:Terminal>
                     <a:Timestamp>2017-03-13T10:28:00</a:Timestamp>
                  </a:Event>
               </a:Events>
               <a:ReceiptDate>2017-03-13T10:28:00</a:ReceiptDate>
               <a:ReceivedBy>MAJEWSKI</a:ReceivedBy>
               <a:ReceivingTerminal>Warszawa</a:ReceivingTerminal>
               <a:SendingTerminal>Warszawa</a:SendingTerminal>
               <a:SentDate>2017-03-10T00:00:00</a:SentDate>
               <a:ShipmentNumber>26713587389</a:ShipmentNumber>
               <a:Status>FOUND</a:Status>
            </a:Shipment>
         </GetShipmentsResult>
      </GetShipmentsResponse>
   </s:Body>
</s:Envelope>
"""
}

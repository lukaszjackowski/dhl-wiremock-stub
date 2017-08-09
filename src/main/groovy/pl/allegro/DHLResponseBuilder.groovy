package pl.allegro

import groovy.text.SimpleTemplateEngine


class DHLResponseBuilder {

    private List<String> shipmentNumbers

    def withShipmentNumbers(List<String> shipmentNumbers) {
        this.shipmentNumbers = shipmentNumbers
        this
    }

    String build() {
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(SHIPMENT)
        def shipments = shipmentNumbers.collect { shipmentNumber -> template.make(["shipmentNumber": shipmentNumber]) }.join("")
        engine.createTemplate(GET_SHIPMENTS).make(["shipments": shipments])
    }

    static DHLResponseBuilder builder() {
        new DHLResponseBuilder()
    }

    private static final String GET_SHIPMENTS = '''
        <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
           <s:Body>
              <GetShipmentsResponse xmlns="http://www.dhl.com.pl/tntwebservice/2014/04">
                 <GetShipmentsResult xmlns:a="http://schemas.datacontract.org/2004/07/com.dhl.pl.TnTWebService.v2.Interface" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
                    ${shipments}
                 </GetShipmentsResult>
              </GetShipmentsResponse>
           </s:Body>
        </s:Envelope>
    '''

    private static final String SHIPMENT = '''
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
           <a:ShipmentNumber>${shipmentNumber}</a:ShipmentNumber>
           <a:Status>FOUND</a:Status>
        </a:Shipment>
    '''

}

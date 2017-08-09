package pl.allegro

import com.github.tomakehurst.wiremock.common.FileSource
import com.github.tomakehurst.wiremock.extension.Parameters
import com.github.tomakehurst.wiremock.extension.ResponseTransformer
import com.github.tomakehurst.wiremock.http.Request
import com.github.tomakehurst.wiremock.http.Response


class DHLResponseTransformer extends ResponseTransformer {

    @Override
    Response transform(Request request, Response response, FileSource files, Parameters parameters) {
        def responseBody = DHLResponseBuilder.builder()
                .withShipmentNumbers(getShipmentNumbers(request.bodyAsString))
                .build()
        return Response.Builder.like(response).body(responseBody).build()
    }

    static List<String> getShipmentNumbers(String body) {
        def xml = new XmlSlurper().parseText(body)
        def numbers = xml.Body.GetShipments.shipmentNumbers.string
        return numbers*.text()
    }

    @Override
    String getName() {
        return "dhl-response-transformer"
    }

}

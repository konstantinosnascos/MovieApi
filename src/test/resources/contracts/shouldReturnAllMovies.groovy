package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description ("Should return an array of movies")
    request {
        method GET()
        url("/api/v1/movies")
    }
    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body([
                id: $(anyNumber()),
                title: $(anyNonEmptyString()),
                directorName: $(anyNonEmptyString()),
                status: $(anyNonEmptyString()),
                isLoaned: $(anyBoolean())
        ])
        }
    }
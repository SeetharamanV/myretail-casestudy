package com.myretail.products

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import spock.lang.Specification

abstract class AbstractSpecification extends Specification {
    protected def easyRandom = new EasyRandom(
            new EasyRandomParameters()
                    .seed(1234L)
                    .stringLengthRange(5, 30)
                    .collectionSizeRange(1, 10)
    )

    protected def PRODUCT_ID = 123
}

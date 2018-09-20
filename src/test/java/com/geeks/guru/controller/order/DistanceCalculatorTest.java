package com.geeks.guru.controller.order;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.geeks.guru.service.order.DistanceCalculator;

public class DistanceCalculatorTest extends DeliverOrderTest {

    @InjectMocks
    @Autowired
    private DistanceCalculator distanceCalculator;
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testDistanceCalculatorException() throws Exception {
	expectedException.expect(NullPointerException.class);
	distanceCalculator.calculateDistance(null, null);
    }
}

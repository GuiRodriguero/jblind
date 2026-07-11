package com.gui.jblind;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class TestBase {

	private final Map<Class<?>, MockedStatic<?>> mockedStatics = new HashMap<>();

	@BeforeEach
	public void setUpTest() {
		MockitoAnnotations.openMocks(this);

		getMockedStatic(UUID.class).when(UUID::randomUUID).thenReturn(new UUID(0, 0));

		this.init();
	}

	@AfterEach
	public void tearDownTest() {
		mockedStatics.values().stream().filter(item -> !item.isClosed()).forEach(MockedStatic::close);
		mockedStatics.clear();
	}

	public abstract void init();

	public static <T> T valid(Class<T> type) {
		return Instancio.of(type).create();
	}

	public static <T> List<T> valid(Class<T> type, Integer size) {
		return Instancio.ofList(type).size(size).create();
	}

	protected <T> MockedStatic<T> getMockedStatic(Class<T> classToMock) {
		return (MockedStatic<T>) mockedStatics.computeIfAbsent(classToMock, Mockito::mockStatic);
	}

}

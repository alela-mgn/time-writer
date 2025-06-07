package com.leon.timewriter;

import com.leon.timewriter.entity.Time;
import com.leon.timewriter.repository.TimeRepository;
import com.leon.timewriter.service.RetryingService;
import com.leon.timewriter.service.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class TimeWriterApplicationTests {

	private TimeRepository repository;
	private RetryingService retryingService;
	private TimeService service;

	@BeforeEach
	public void setup() {
		repository = mock(TimeRepository.class);
		retryingService = mock(RetryingService.class);
		service = new TimeService(repository, retryingService);
	}

	@Test
	public void shouldBufferIfDbUnavailable() {
		when(retryingService.isDbAvailable()).thenReturn(false);
		service.writeTime();
		verify(repository, never()).save(any());
	}

	@Test
	public void shouldSaveBufferedOnRecovery() {
		when(retryingService.isDbAvailable()).thenReturn(false);
		service.writeTime();

		when(retryingService.isDbAvailable()).thenReturn(true);
		service.writeTime();

		verify(repository, times(2)).save(any());
	}

	@Test
	public void shouldHandleSaveExceptionAndBuffer() {
		when(retryingService.isDbAvailable()).thenReturn(true);
		doThrow(new RuntimeException("DB error")).when(repository).save(any());

		service.writeTime();
		verify(repository).save(any());
	}

	@Test
	public void shouldReturnAllTimestamps() {
		when(repository.findAll()).thenReturn(List.of(new Time(LocalDateTime.now())));
		var result = service.getAllTimestamps();
		assertThat(result).hasSize(1);
	}
}

package com.ua.yushchenko.service.prediction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ua.yushchenko.common.SplitMix64RandomGenerator;
import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.model.User;
import com.ua.yushchenko.model.UserPrediction;
import com.ua.yushchenko.repository.PredictionRepository;
import com.ua.yushchenko.repository.UserPredictionRepository;
import com.ua.yushchenko.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class PredictionServiceImplTest {

    @Mock
    private UserPredictionRepository mockUserPredictionRepository;
    @Mock
    private PredictionRepository mockPredictionRepository;
    @Mock
    private UserService mockUserService;
    @Mock
    private SplitMix64RandomGenerator mockRandomGenerator;

    @InjectMocks
    private PredictionServiceImpl unit;

    private User user;
    private Prediction prediction;
    private UserPrediction userPrediction;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setChatId(12345L);

        prediction = new Prediction();
        prediction.setId(1L);
        prediction.setText("Test Prediction");

        userPrediction = new UserPrediction();
        userPrediction.setUser(user);
        userPrediction.setPrediction("Test Prediction");
        userPrediction.setSentAt(LocalDateTime.now());
    }

    @Test
    void testGetPredictionByText_Found() {
        when(mockPredictionRepository.findPredictionByText("Test Prediction"))
                .thenReturn(Optional.of(prediction));

        Optional<Prediction> result = unit.getPredictionByText("Test Prediction");

        assertTrue(result.isPresent());
        assertEquals(prediction, result.get());
    }

    @Test
    void testGetPredictionByText_NotFound() {
        when(mockPredictionRepository.findPredictionByText("Unknown"))
                .thenReturn(Optional.empty());

        Optional<Prediction> result = unit.getPredictionByText("Unknown");

        assertFalse(result.isPresent());
    }

    @Test
    void testGenerateQuickPrediction_NormalCase() {
        when(mockUserService.getLastPrediction(12345L)).thenReturn("Old Prediction");
        when(mockPredictionRepository.findAll()).thenReturn(List.of(prediction));

        Prediction result = unit.generateQuickPrediction(12345L);

        assertEquals(prediction, result);
    }

    @Test
    void testGenerateQuickPrediction_SinglePrediction() {
        when(mockUserService.getLastPrediction(12345L)).thenReturn("Test Prediction");
        when(mockPredictionRepository.findAll()).thenReturn(List.of(prediction));

        Prediction result = unit.generateQuickPrediction(12345L);

        assertEquals(prediction, result);
    }

    @Test
    void testGenerateUniquePrediction_NormalCase() {
        when(mockUserService.findByChatId(12345L)).thenReturn(user);
        when(mockPredictionRepository.findAll()).thenReturn(List.of(prediction));
        when(mockUserPredictionRepository.findAllByUserOrderBySentAtDesc(user))
                .thenReturn(List.of());

        Prediction result = unit.generateUniquePrediction(12345L);

        assertEquals(prediction, result);
    }

    @Test
    void testSaveUserPrediction() {
        unit.saveUserPrediction(user, "Test Prediction");

        verify(mockUserPredictionRepository).save(any(UserPrediction.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test Prediction", "Another Prediction"})
    void testSaveUserPrediction_Parametrized(String predictionText) {
        unit.saveUserPrediction(user, predictionText);

        verify(mockUserPredictionRepository).save(any(UserPrediction.class));
    }

    @Test
    void shouldReturnFirstPredictionWhenFilteredListIsEmpty() {
        when(mockUserService.getLastPrediction(12345L)).thenReturn("Same Prediction");

        Prediction p1 = new Prediction();
        p1.setText("Same Prediction");

        Prediction p2 = new Prediction();
        p2.setText("Same Prediction");

        when(mockPredictionRepository.findAll()).thenReturn(List.of(p1, p2));

        Prediction result = unit.generateQuickPrediction(12345L);

        assertEquals(p1, result);
    }

    @Test
    void shouldReturnRandomPredictionFromFilteredList() {
        when(mockUserService.getLastPrediction(12345L)).thenReturn("Old Prediction");

        Prediction p1 = new Prediction();
        p1.setText("Old Prediction");

        Prediction p2 = new Prediction();
        p2.setText("New Prediction");

        when(mockPredictionRepository.findAll()).thenReturn(List.of(p1, p2));
        when(mockRandomGenerator.nextInt(1)).thenReturn(0);

        Prediction result = unit.generateQuickPrediction(12345L);

        assertEquals("New Prediction", result.getText());
    }

}


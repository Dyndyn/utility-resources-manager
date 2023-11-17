package com.dyndyn.urm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dyndyn.urm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsumptionHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumptionHistoryDTO.class);
        ConsumptionHistoryDTO consumptionHistoryDTO1 = new ConsumptionHistoryDTO();
        consumptionHistoryDTO1.setId(1L);
        ConsumptionHistoryDTO consumptionHistoryDTO2 = new ConsumptionHistoryDTO();
        assertThat(consumptionHistoryDTO1).isNotEqualTo(consumptionHistoryDTO2);
        consumptionHistoryDTO2.setId(consumptionHistoryDTO1.getId());
        assertThat(consumptionHistoryDTO1).isEqualTo(consumptionHistoryDTO2);
        consumptionHistoryDTO2.setId(2L);
        assertThat(consumptionHistoryDTO1).isNotEqualTo(consumptionHistoryDTO2);
        consumptionHistoryDTO1.setId(null);
        assertThat(consumptionHistoryDTO1).isNotEqualTo(consumptionHistoryDTO2);
    }
}

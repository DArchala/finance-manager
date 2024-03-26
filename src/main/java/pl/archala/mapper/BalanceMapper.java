package pl.archala.mapper;

import org.springframework.stereotype.Component;
import pl.archala.dto.balance.AddBalanceDTO;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.entity.Balance;

@Component
public class BalanceMapper {

    public Balance toEntity(AddBalanceDTO addBalanceDTO) {
        return new Balance();
    }

    public GetBalanceDTO toGetDto(Balance balance) {
        return new GetBalanceDTO();
    }

}

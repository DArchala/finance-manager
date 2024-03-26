package pl.archala.mapper;

import org.springframework.stereotype.Component;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.entity.Balance;

@Component
public class BalanceMapper {

    public GetBalanceDTO toGetDto(Balance balance) {
        return new GetBalanceDTO(balance.getId(), balance.getValue());
    }

}

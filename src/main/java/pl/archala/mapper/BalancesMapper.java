package pl.archala.mapper;

import org.mapstruct.Mapper;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.entity.Balance;

@Mapper(componentModel = "spring")
public interface BalancesMapper {

    GetBalanceDTO toGetDto(Balance balance);

}

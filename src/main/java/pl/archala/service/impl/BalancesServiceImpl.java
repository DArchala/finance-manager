package pl.archala.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.archala.repository.BalancesRepository;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.entity.Balance;
import pl.archala.entity.User;
import pl.archala.exception.UserAlreadyContainsBalance;
import pl.archala.mapper.BalanceMapper;
import pl.archala.repository.UsersRepository;
import pl.archala.service.BalancesService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalancesServiceImpl implements BalancesService {

    private final BalancesRepository balancesRepository;
    private final UsersRepository usersRepository;
    private final BalanceMapper balanceMapper;

    @Override
    public GetBalanceDTO findById(Long id) {
        Balance balance = findBalanceById(id);
        return balanceMapper.toGetDto(balance);
    }

    @Override
    public GetBalanceDTO save(String username) throws UserAlreadyContainsBalance {
        User user = findUserByUsername(username);

        if (user.getBalance() != null) {
            throw new UserAlreadyContainsBalance("User %s already contains balance, it is not possible to create next one.".formatted(user.getUsername()));
        }

        Balance balance = new Balance();
        balance.setUser(user);

        Balance savedBalance = balancesRepository.save(balance);
        user.setBalance(savedBalance);

        return balanceMapper.toGetDto(savedBalance);
    }

    private Balance findBalanceById(Long id) {
        Optional<Balance> optionalBalance = balancesRepository.findById(id);
        if (optionalBalance.isEmpty()) {
            throw new EntityNotFoundException("Balance with id %d does not exist".formatted(id));
        }
        return optionalBalance.get();
    }

    private User findUserByUsername(String username) {
        Optional<User> optionalUser = usersRepository.findUserByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with name %s does not exist".formatted(username));
        }
        return optionalUser.get();
    }

}

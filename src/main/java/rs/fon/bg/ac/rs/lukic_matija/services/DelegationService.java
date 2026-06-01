package rs.fon.bg.ac.rs.lukic_matija.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fon.bg.ac.rs.lukic_matija.domain.Delegation;
import rs.fon.bg.ac.rs.lukic_matija.domain.Match;
import rs.fon.bg.ac.rs.lukic_matija.domain.Referee;
import rs.fon.bg.ac.rs.lukic_matija.dtos.delegationDtos.DelegationAddDto;
import rs.fon.bg.ac.rs.lukic_matija.dtos.delegationDtos.DelegationResponseDto;
import rs.fon.bg.ac.rs.lukic_matija.repositories.DelegationRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.MatchRepository;
import rs.fon.bg.ac.rs.lukic_matija.repositories.RefereeRepository;

@Service
public class DelegationService {
    private final DelegationRepository delegationRepository;
    private final RefereeRepository refereeRepository;
    private final MatchRepository matchRepository;

    public DelegationService(DelegationRepository delegationRepository, RefereeRepository refereeRepository, MatchRepository matchRepository) {
        this.delegationRepository = delegationRepository;
        this.refereeRepository = refereeRepository;
        this.matchRepository = matchRepository;
    }

    @Transactional
    public DelegationResponseDto create(DelegationAddDto dAdd){
        Delegation d = dAdd.toEntity();
        Match m = matchRepository.findById(dAdd.matchId())
                .orElseThrow(()->new EntityNotFoundException("Match doesnt exists"));
        Referee r = refereeRepository.findById(dAdd.refereeId())
                .orElseThrow(()-> new EntityNotFoundException("Referee doesnt exists"));
        d.setMatch(m);
        d.setReferee(r);
        return DelegationResponseDto.fromEntity(delegationRepository.save(d));
    }
    @Transactional
    public DelegationResponseDto updateScore(Long id, double score){
        Delegation d = delegationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Delegation doesnt exists"));
        d.setOfficiatingScore(score);
        return DelegationResponseDto.fromEntity(delegationRepository.save(d));
    }
}

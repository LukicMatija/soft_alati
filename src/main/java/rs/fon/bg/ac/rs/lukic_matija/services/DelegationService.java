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

/**
 * Service for managing match official delegations.
 * Handles the creation of referee assignments to specific matches and
 * the evaluation updates of their officiating performance.
 * @author Matija Lukic
 */
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

    /**
     * Creates a new delegation by assigning a certified referee to a scheduled match.
     * @param dAdd DelegationAddDto data transfer object containing the match ID, referee ID, and assigned role.
     * @return DelegationResponseDto containing the details of the successfully saved delegation.
     * @throws jakarta.persistence.EntityNotFoundException If the associated match or referee cannot be found.
     */
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

    /**
     * Updates the post-match officiating performance score for an existing delegation.
     * @param id unique identifier of the delegation to be updated.
     * @param score the numerical evaluation grade given to the referee.
     * @return DelegationResponseDto containing the updated delegation record data.
     * @throws jakarta.persistence.EntityNotFoundException If the delegation with the given ID does not exist.
     */
    @Transactional
    public DelegationResponseDto updateScore(Long id, double score){
        Delegation d = delegationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Delegation doesnt exists"));
        d.setOfficiatingScore(score);
        return DelegationResponseDto.fromEntity(delegationRepository.save(d));
    }
}

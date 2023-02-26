package Jay.BoardP.service;


import Jay.BoardP.controller.dto.PenaltyListDto;
import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.Member;
import Jay.BoardP.domain.Penalty;
import Jay.BoardP.controller.form.PenaltyForm;
import Jay.BoardP.repository.BoardRepository;
import Jay.BoardP.repository.MemberRepository;
import Jay.BoardP.repository.PenaltyRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    public Boolean exist(Long memberId, Long boardId, PenaltyForm penaltyForm) {

        Member member = memberRepository.findMember(memberId);

        Board board = boardRepository.findBoard(boardId);

        Boolean isExist = false;

        if (penaltyRepository.exist(memberId, boardId).orElse(null) != null)
            isExist = true;

        penaltyRepository.save(Penalty.createPenalty(board, member, penaltyForm));

        return isExist;

    }

    public List<PenaltyListDto> getPenalties(Long boardId) {
        List<Penalty> penalties = penaltyRepository.findAllByBoardId(boardId);

        return penalties.stream().map(
            PenaltyListDto::createPenaltyDto
        ).collect(Collectors.toList());
    }

}

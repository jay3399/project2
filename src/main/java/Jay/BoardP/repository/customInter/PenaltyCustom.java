package Jay.BoardP.repository.customInter;

import Jay.BoardP.domain.Penalty;
import java.util.List;
import java.util.Optional;

public interface PenaltyCustom {


    public Optional<Penalty> exist(Long memberId, Long boardId);



}

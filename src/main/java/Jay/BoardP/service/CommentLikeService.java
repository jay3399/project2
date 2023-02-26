package Jay.BoardP.service;


import Jay.BoardP.controller.dto.CommentDisLikeDto;
import Jay.BoardP.controller.dto.CommentLikeDto;
import Jay.BoardP.domain.BoardComment;
import Jay.BoardP.domain.CommentDisLike;
import Jay.BoardP.domain.CommentLike;
import Jay.BoardP.domain.Member;
import Jay.BoardP.repository.CommentDisLikeRepository;
import Jay.BoardP.repository.CommentLikeRepository;
import Jay.BoardP.repository.CommentRepositoryRepository;
import Jay.BoardP.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentLikeService {


    private final CommentLikeRepository commentLikeRepository;
    private final CommentDisLikeRepository commentDisLikeRepository;
    private final CommentRepositoryRepository repository;

    private final MemberRepository memberRepository;


    @CacheEvict(value = "comment", key = "#boardId")
    public Boolean pushLikeButton(Long memberId, Long commentId, Long boardId) {

        Member member = memberRepository.findMember(memberId);
        BoardComment boardComment = repository.findComment(commentId);

        commentLikeRepository.exist(member.getId(),
            boardComment.getId()).ifPresentOrElse(
            (commentLike) -> {
                commentLikeRepository.deleteById(commentLike.getId());
                commentLike.removeComment(boardComment,
                    member);
            },
            () -> {
                CommentLike commentLike = CommentLike.createCommentLike(
                    boardComment,
                    member);
                commentLikeRepository.save(commentLike);
            });
        return true;
    }


    public Boolean pushDisLikeButton(Long memberId, Long commentId) {

        Member member = memberRepository.findMember(memberId);
        BoardComment boardComment = repository.findComment(commentId);

        commentDisLikeRepository.exist(member.getId(),
            boardComment.getId()).ifPresentOrElse(
            (commentDisLike) -> {
                commentDisLikeRepository.deleteById(commentDisLike.getId());
                commentDisLike.removeComment(boardComment,
                    member);
            },
            () -> {
                CommentDisLike commentLike = CommentDisLike.createCommentDisLike(
                    boardComment,
                    member);
                commentDisLikeRepository.save(commentLike);
            });
        return true;
    }

//    public Boolean pushLikeButton(CommentLikeDto commentLikeDto) {
//
//
//        commentLikeRepository.exist(commentLikeDto.getMember().getId(),
//            commentLikeDto.getBoardComment()
//                .getId()).ifPresentOrElse(
//            (commentLike) -> {
//                repository.deleteById(commentLike.getId());
//                commentLike.removeComment(commentLikeDto.getBoardComment(),
//                    commentLikeDto.getMember());
//            },
//            () -> {
//                BoardComment comment = getComment(commentLikeDto);
//                CommentLike commentLike = CommentLike.createCommentLike(
//                    comment,
//                    commentLikeDto.getMember());
//                repository.save(commentLike);
//            });
//        return true;
//    }

//    public Boolean pushDisLikeButton(CommentDisLikeDto commentDisLikeDto) {
//        commentDisLikeRepository.exist(commentDisLikeDto.getMember().getId(),
//            commentDisLikeDto.getBoardComment()
//                .getId()).ifPresentOrElse(
//            commentDisLike -> {
//                repository.deleteById(commentDisLike.getId());
//                commentDisLike.removeComments(commentDisLikeDto.getBoardComment(),
//                    commentDisLikeDto.getMember());
//            }
//            ,
//            () -> {
//                BoardComment comment = getComment(commentDisLikeDto);
//                CommentDisLike commentDisLike = CommentDisLike.createCommentDisLike(comment,
//                    commentDisLikeDto.getMember());
//                postDisLikeRepository.save(commentDisLike);
//            }
//
//        );
//        return true;
//    }


    public BoardComment getComment(CommentLikeDto commentLikeDto) {
        return repository.findComment(commentLikeDto.getBoardComment().getId());
    }

    public BoardComment getComment(CommentDisLikeDto commentLikeDto) {
        return repository.findComment(commentLikeDto.getBoardComment().getId());
    }

    public BoardComment getCommentV2(Long boardId) {
        return repository.findComment(boardId);
    }


}

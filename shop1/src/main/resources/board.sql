create table board (
	num int primary key, 	
	writer varchar(30),   	
	pass varchar(20), 		
	title varchar(100),		
	content varchar(2000),	
	file1 varchar(200),		
	boardid varchar(2),		
	regdate datetime,		
	readcnt int(10),		
	grp int,				
	grplevel int(3),		
	grpstep int(5)			
);

select * from board

create table board (
	num int primary key, 	--게시글번호. 기본키
	writer varchar(30),   	--작성자 이름
	pass varchar(20), 		--비밀번호
	title varchar(100),		--글제목
	content varchar(2000),	--글내용
	file1 varchar(200),		--첨부파일명
	boardid varchar(2),		--게시판종류:1.공지사항,2.자유게시판,3.QnA
	regdate datetime,		--게시글등록일시
	readcnt int(10),		--조회수. 상세보기 시 1씩 증가
	grp int,				--답글 작성시 원글의 게시글번호
	grplevel int(3),		--답글의 레벨.
	grpstep int(5)			--그룹의 출력 순서
);
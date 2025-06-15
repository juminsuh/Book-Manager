-- 도서 정보
create table book(
    book_id varchar(20),
    title varchar(30) not null,
	author varchar(20) not null,
	publisher varchar(20),
	published_year year,
	genre varchar(10) check (genre in ('소설', '시/에세이', '자기계발', 'IT/컴퓨터', '경제/경영', '여행')),
	primary key (book_id) 
);

-- 회원 정보
create table member(
	member_id int,
	mname varchar(30),
	email varchar(50) unique,
	phone varchar(20) unique,
	primary key (member_id)
);

-- 대출 기록
create table loan(
	loan_id int,
	book_id varchar(20) not null,
	member_id int not null,
	loan_date date not null,
	return_date date default null, -- 아직 반납 안함
	primary key (loan_id),
	foreign key (book_id) references book(book_id) on delete cascade,
	foreign key (member_id) references member(member_id) on delete cascade
);

-- 연체 기록
create table penalty(
	penalty_id int,
	loan_id int,
	penalty_date int not null, -- 연체 오버 날짜
	primary key (penalty_id, loan_id),
	foreign key (loan_id) references loan(loan_id) on delete cascade
);

-- index: book(genre)
-- loan_id와 penalty_id는 인조 식별자이므로, 각각의 테이블에서 book_id, member_id에 인덱스를 걸어주는 것이 성능 향상에 도움이 됨

create index genre_index on book(genre);
create index book_id_index_loan on loan(book_id);
create index member_id_index_loan on loan(member_id);
create index loan_date_index_loan on loan(loan_date);

-- view: 최신 등록 도서 정보 조회
create view recent_book as
select book_id, title, author, published_year
from book
where published_year = year(current_date);

-- view: 회원별 대출 횟수
create view most_reader as
select member_id, count(*) as num_read
from loan
group by member_id;





CREATE TABLE member(
    m_id                 VARCHAR2(20)       CONSTRAINT member_id_pk           PRIMARY KEY,
    m_pwd                VARCHAR2(100)      CONSTRAINT member_pwd_nn          NOT NULL,
    m_name               VARCHAR2(30)       CONSTRAINT member_name_nn         NOT NULL,
    m_intro_short        VARCHAR2(100)                                        NULL,
    m_pic                VARCHAR2(100)                                        NULL,
    m_registered_at      DATE               DEFAULT SYSDATE,
    enabled              CHAR(1)            DEFAULT '1'
);

--CREATE UNIQUE INDEX IDX_member_PK ON member(m_id ASC);
ALTER INDEX MEMBER_ID_PK RENAME TO IDX_member_PK;

--------------------------------------------------------------------------------

CREATE TABLE member_auth (
    m_id        VARCHAR2(20)     NOT NULL,
    auth        VARCHAR2(50)     NOT NULL,
    CONSTRAINT fk_member_auth FOREIGN KEY(m_id) REFERENCES member(m_id)  ON DELETE CASCADE
);

--------------------------------------------------------------------------------

CREATE TABLE post(
p_id                NUMBER(10)          CONSTRAINT post_p_id_pk        PRIMARY KEY,
m_id                VARCHAR2(20)        CONSTRAINT post_m_id_nn        NOT NULL
                                        CONSTRAINT post_m_id_fk        REFERENCES member(m_id) ON DELETE CASCADE,
p_title             VARCHAR2(75)        CONSTRAINT post_p_title_nn     NOT NULL,
p_public_yn         CHAR(1)             DEFAULT 'y',
p_published_at      DATE                DEFAULT SYSDATE,
p_updated_at        DATE                                               NULL,
p_content           CLOB                                               NULL,
p_view_cnt          NUMBER              DEFAULT 0,
p_like_cnt          NUMBER              DEFAULT 0,
p_comment_cnt       NUMBER              DEFAULT 0,
t_name              VARCHAR2(30)                                       NULL,
p_pic               VARCHAR2(100)                                      NULL
);

--CREATE UNIQUE INDEX IDX_post_PK ON post(p_id ASC);
ALTER INDEX POST_P_ID_PK RENAME TO IDX_post_PK;
    
CREATE SEQUENCE post_seq
INCREMENT BY 1
START WITH 1
NOMAXVALUE
NOCACHE;

--------------------------------------------------------------------------------

CREATE TABLE post_like (
    p_id        NUMBER(10)     CONSTRAINT like_pid_nn       NOT NULL
                               CONSTRAINT like_pid_FK       REFERENCES post(p_id) ON DELETE CASCADE,
    m_id        VARCHAR2(20)   CONSTRAINT like_mid_nn       NOT NULL
                               CONSTRAINT like_mid_FK       REFERENCES member(m_id) ON DELETE CASCADE,
    CONSTRAINT like_mid_pid_PK   PRIMARY KEY (p_id, m_id)
);

--CREATE UNIQUE INDEX IDX_post_like_PK ON post(p_id,m_id ASC);
ALTER INDEX like_mid_pid_PK RENAME TO IDX_post_like_PK;

--------------------------------------------------------------------------------

CREATE TABLE post_comment (
c_id               NUMBER(10)         CONSTRAINT comment_cid_PK                PRIMARY KEY,
p_id               NUMBER(10)         CONSTRAINT comment_pid_nn                NOT NULL
                                      CONSTRAINT comment_pid_FK                REFERENCES post(p_id) ON DELETE CASCADE,
c_published_at     DATE               DEFAULT SYSDATE,
c_updated_at       DATE                                                        NULL,
c_content          VARCHAR2(600)     CONSTRAINT comment__ccontent_nn          NOT NULL,
m_id               VARCHAR2(20)      CONSTRAINT comment_mid_FK                REFERENCES member(m_id) ON DELETE CASCADE
);

--CREATE UNIQUE INDEX IDX_post_comment_PK ON post_comment(p_id DESC, c_id ASC);
ALTER INDEX COMMENT_CID_PK RENAME TO IDX_post_comment_PK;

CREATE SEQUENCE post_comment_seq
INCREMENT BY 1
START WITH 1
NOMAXVALUE
NOCACHE;

--------------------------------------------------------------------------------

CREATE TABLE post_attach (
    uuid            VARCHAR2(100)      NOT NULL,
    uploadPath      VARCHAR2(200)      NOT NULL,
    fileName        VARCHAR2(100)      NOT NULL,
    fileType        CHAR(1)            DEFAULT 'I',
    p_id            NUMBER(10)
);

ALTER TABLE post_attach
ADD CONSTRAINT post_attach_uuid_PK PRIMARY KEY (uuid);

ALTER TABLE post_attach
ADD CONSTRAINT post_attach_pid_FK FOREIGN KEY (p_id) REFERENCES post(p_id) ON DELETE CASCADE;



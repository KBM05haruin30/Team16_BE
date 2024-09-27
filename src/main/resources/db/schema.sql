CREATE TABLE member
(
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    kakao_member_id BIGINT NOT NULL UNIQUE,
    nickname VARCHAR(255),
    thumbnail_image_url VARCHAR(255),
    profile_image_url VARCHAR(255)
);
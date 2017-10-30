/*  Updated On: 2017/10/27  */
    
# Creates database survey_feedback_db if not exists.  
CREATE DATABASE IF NOT EXISTS survey_feedback_db;

# Use database survey_feedback_db
USE survey_feedback_db;

# Creates user_roles table if not exists
CREATE TABLE IF NOT EXISTS user_roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role VARCHAR(20)
);

# create default value for role_id
# Creates user_details table if not exists
CREATE TABLE IF NOT EXISTS user_details (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(128) NULL,
    token VARCHAR(25) NULL,
    name VARCHAR(100) NOT NULL,
    dob DATE NULL,
    gender VARCHAR(1) CHECK(category IN ("M", "F", "O")),
    role_id INT,
    created_date DATE NOT NULL,
    updated_date DATE NOT NULL,
    FOREIGN KEY(role_id) REFERENCES user_roles(role_id) ON UPDATE CASCADE ON DELETE CASCADE
);

# Creates survey table if not exists
CREATE TABLE IF NOT EXISTS survey (
    survey_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    survey_name VARCHAR(500) NOT NULL,
    status ENUM("live", "not live") NOT NULL,
    created_date DATE NOT NULL,
    updated_date DATE NOT NULL,
    FOREIGN KEY(user_id) REFERENCES user_details(user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

# Creates survey_labels table if not exists.
CREATE TABLE IF NOT EXISTS survey_labels (
    label_id INT PRIMARY KEY AUTO_INCREMENT,
    survey_id INT NOT NULL,
    survey_label VARCHAR(50) NOT NULL,
    FOREIGN KEY(survey_id) REFERENCES survey(survey_id) ON UPDATE CASCADE ON DELETE CASCADE
);


# Creates survey_questions table if not exists
CREATE TABLE IF NOT EXISTS survey_questions (
    ques_id INT PRIMARY KEY AUTO_INCREMENT,
    survey_id INT NOT NULL,
    question TEXT NOT NULL,
    created_date DATE NOT NULL,
    updated_date DATE NOT NULL,
    FOREIGN KEY(survey_id) REFERENCES survey(survey_id) ON UPDATE CASCADE ON DELETE CASCADE
);

# Creates question_options table if not exists
CREATE TABLE IF NOT EXISTS question_options (
    ques_id INT NOT NULL,
    option_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    option_value TEXT NOT NULL,
    option_type ENUM("text", "image", "video") NOT NULL, 
    created_date DATE NOT NULL,
    updated_date DATE NOT NULL,
    FOREIGN KEY(ques_id) REFERENCES survey_questions(ques_id) ON UPDATE CASCADE ON DELETE CASCADE
);

# Creates survey_responses table if not exists.
CREATE TABLE IF NOT EXISTS survey_responses (
    survey_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY(survey_id, user_id),
    FOREIGN KEY(user_id) REFERENCES user_details(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(survey_id) REFERENCES survey(survey_id) ON UPDATE CASCADE ON DELETE CASCADE
);

# Creates user_responses table if not exists
CREATE TABLE IF NOT EXISTS user_responses (
    ques_id INT NOT NULL,
    option_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (ques_id, user_id),
    FOREIGN KEY(user_id) REFERENCES user_details(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(ques_id) REFERENCES survey_questions(ques_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(option_id) REFERENCES question_options(option_id) ON UPDATE CASCADE ON DELETE CASCADE
);
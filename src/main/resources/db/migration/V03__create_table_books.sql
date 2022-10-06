CREATE TABLE tb_book(
  id INT(10) AUTO_INCREMENT PRIMARY key,
  author VARCHAR(255) NOT NULL,
  launch_date datetime(6) NOT NULL,
  price decimal(65,2) NOT NULL,
  title VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

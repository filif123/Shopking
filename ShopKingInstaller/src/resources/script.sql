-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema shopking
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema shopking
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `shopking` DEFAULT CHARACTER SET utf8 ;
USE `shopking` ;

-- -----------------------------------------------------
-- Table `shopking`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `meno` VARCHAR(255) NULL,
  `priezvisko` VARCHAR(255) NULL,
  `nickname` VARCHAR(255) NULL,
  `hash` BIGINT NULL,
  `usertype` ENUM("ADMIN", "POKLADNIK") NULL,
  `suma_v_pokladnici` FLOAT NULL,
  `vykonal_zavierku` DATETIME NULL,
  `deleted` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`categories` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nazov` VARCHAR(255) NULL,
  `pristupne_pre_mladistvych` TINYINT NULL DEFAULT 1,
  `povolene_stravne_listky` TINYINT NULL DEFAULT 1,
  `deleted` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`obchod`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`obchod` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `obchodny_nazov` VARCHAR(255) NULL,
  `nazov_obchodu` VARCHAR(255) NULL,
  `sidlo_mesto` VARCHAR(255) NULL,
  `sidlo_ulica` VARCHAR(100) NULL,
  `sidlo_psc` VARCHAR(10) NULL,
  `sidlo_cislo` VARCHAR(5) NULL,
  `ico` VARCHAR(20) NULL,
  `dic` VARCHAR(20) NULL,
  `icdph` VARCHAR(20) NULL,
  `prevadzka_mesto` VARCHAR(255) NULL,
  `prevadzka_ulica` VARCHAR(100) NULL,
  `prevadzka_psc` VARCHAR(10) NULL,
  `prevadzka_cislo` VARCHAR(5) NULL,
  `logo_path` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`tovary`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`tovary` (
  `plu` INT(11) NOT NULL AUTO_INCREMENT,
  `nazov` VARCHAR(255) NULL,
  `cena` FLOAT NULL,
  `dph` VARCHAR(6) NULL,
  `ean` VARCHAR(20) NULL,
  `jednotka` VARCHAR(2) NULL,
  `id_category` INT(11) NOT NULL,
  `deleted` TINYINT NULL DEFAULT 0,
  `zlava_nova_cena` FLOAT NULL,
  `zlava_povodne_mnozstvo` INT NULL,
  `zlava_nove_mnozstvo` INT NULL,
  `zlava_min_mnozstvo` INT NULL,
  PRIMARY KEY (`plu`),
  INDEX `fk_tovary_categories1_idx` (`id_category` ASC) ,
  UNIQUE INDEX `plu_UNIQUE` (`plu` ASC) ,
  CONSTRAINT `fk_tovary_categories1`
    FOREIGN KEY (`id_category`)
    REFERENCES `shopking`.`categories` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`doklady`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`doklady` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `cas_nakupu` DATETIME NULL,
  `id_pokladnik` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_doklady_users1_idx` (`id_pokladnik` ASC) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  CONSTRAINT `fk_doklady_users1`
    FOREIGN KEY (`id_pokladnik`)
    REFERENCES `shopking`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`nakupeny_tovar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`nakupeny_tovar` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `mnozstvo` FLOAT NULL,
  `nove_mnozstvo` INT NULL DEFAULT NULL,
  `jednotkova_cena` FLOAT NULL,
  `id_doklad` INT(11) NOT NULL,
  `plu_tovar` INT(11) NOT NULL,
  `stornovane_kedy` DATETIME NULL DEFAULT NULL,
  `stornovane_kym` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_nakupy_doklady1_idx` (`id_doklad` ASC) ,
  INDEX `fk_nakupy_tovary1_idx` (`plu_tovar` ASC) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  INDEX `fk_nakupeny_tovar_users1_idx` (`stornovane_kym` ASC) ,
  CONSTRAINT `fk_nakupy_doklady1`
    FOREIGN KEY (`id_doklad`)
    REFERENCES `shopking`.`doklady` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nakupy_tovary1`
    FOREIGN KEY (`plu_tovar`)
    REFERENCES `shopking`.`tovary` (`plu`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nakupeny_tovar_users1`
    FOREIGN KEY (`stornovane_kym`)
    REFERENCES `shopking`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`pokladnice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`pokladnice` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `otvorena` TINYINT NULL DEFAULT 0,
  `id_pokladnik` INT(11) NULL DEFAULT NULL,
  `dkp` VARCHAR(30) NULL,
  `ip` VARCHAR(30) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_pokladnice_users1_idx` (`id_pokladnik` ASC) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  CONSTRAINT `fk_pokladnice_users1`
    FOREIGN KEY (`id_pokladnik`)
    REFERENCES `shopking`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`rocne_zavierky`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`rocne_zavierky` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `datum` DATETIME NULL,
  `interval_od` DATE NULL,
  `interval_do` DATE NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`mesacne_zavierky`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`mesacne_zavierky` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `datum` DATETIME NULL,
  `id_rocna_zavierka` INT(11) NULL,
  `interval_od` DATE NULL,
  `interval_do` DATE NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  INDEX `fk_mesacne_zavierky_rocne_zavierky1_idx` (`id_rocna_zavierka` ASC) ,
  CONSTRAINT `fk_mesacne_zavierky_rocne_zavierky1`
    FOREIGN KEY (`id_rocna_zavierka`)
    REFERENCES `shopking`.`rocne_zavierky` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`denne_zavierky`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`denne_zavierky` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `cas` DATETIME NULL,
  `id_mesacna_zavierka` INT(11) NULL,
  `id_pokladnik` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  INDEX `fk_denne_zavierky_mesacne_zavierky1_idx` (`id_mesacna_zavierka` ASC) ,
  INDEX `fk_denne_zavierky_users1_idx` (`id_pokladnik` ASC) ,
  CONSTRAINT `fk_denne_zavierky_mesacne_zavierky1`
    FOREIGN KEY (`id_mesacna_zavierka`)
    REFERENCES `shopking`.`mesacne_zavierky` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_denne_zavierky_users1`
    FOREIGN KEY (`id_pokladnik`)
    REFERENCES `shopking`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`global_settings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`global_settings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `pokl_poc_suma` FLOAT NULL,
  `pokl_max_suma` FLOAT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`zakaznici`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`zakaznici` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `ukonceny` TINYINT NULL DEFAULT 0,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shopking`.`nakup_zakaznikov_online`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shopking`.`nakup_zakaznikov_online` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `tovar_plu` INT(11) NOT NULL,
  `zakaznik_id` INT(11) NOT NULL,
  `mnozstvo` FLOAT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  INDEX `fk_nakup_zakaznikov_v_obchode_tovary1_idx` (`tovar_plu` ASC) ,
  INDEX `fk_nakup_zakaznikov_v_obchode_zakaznici1_idx` (`zakaznik_id` ASC) ,
  CONSTRAINT `fk_nakup_zakaznikov_v_obchode_tovary1`
    FOREIGN KEY (`tovar_plu`)
    REFERENCES `shopking`.`tovary` (`plu`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nakup_zakaznikov_v_obchode_zakaznici1`
    FOREIGN KEY (`zakaznik_id`)
    REFERENCES `shopking`.`zakaznici` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

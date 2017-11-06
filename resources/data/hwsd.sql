CREATE DATABASE  IF NOT EXISTS `hwsd` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `hwsd`;
-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: localhost    Database: hwsd
-- ------------------------------------------------------
-- Server version	5.5.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `developer_ID` int(10) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_developer_id_idx` (`developer_ID`),
  CONSTRAINT `fk_developer_id` FOREIGN KEY (`developer_ID`) REFERENCES `developer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (2,1),(1,3);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `developer`
--

DROP TABLE IF EXISTS `developer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `developer` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `surname` varchar(60) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `mail` varchar(45) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `biography` text,
  `headline` varchar(200) DEFAULT NULL,
  `curriculum_pdf_ID` int(10) DEFAULT NULL,
  `curriculumString` varchar(500) DEFAULT NULL,
  `photo_filename` varchar(255) DEFAULT NULL,
  `resume` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UC_username` (`username`),
  KEY `FK_curriculumPdf` (`curriculum_pdf_ID`),
  CONSTRAINT `FK_curriculumPdf` FOREIGN KEY (`curriculum_pdf_ID`) REFERENCES `files` (`ID`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `developer`
--

LOCK TABLES `developer` WRITE;
/*!40000 ALTER TABLE `developer` DISABLE KEYS */;
INSERT INTO `developer` VALUES (1,'John','Bonham','jon','gpp@g.com','0000-00-00','admin','Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur.','Analyst',1,'a.jpg',1,NULL),(2,'Ronnie','Wood','ron','gppbianchi@gmail.com','1992-07-13','admin','Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur.','iOS Developer',NULL,'',NULL,NULL),(3,'Giuseppe','Bianchi','admin','gppbianchi@gmail.com','1992-07-13','admin','Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur.','Javascript Software Engineer',NULL,NULL,NULL,'<p style=\"margin-top: 0.875rem; margin-bottom: 0.875rem; padding: 0px; font-family: Publico, Times, &quot;Times New Roman&quot;, serif; color: rgb(34, 34, 34); font-size: 17px;\">I have been involved in numerous large-scale software releases and understand the importance of time management and open communication. While it may be easy to focus on the technical side of work, I?ve found that being able to express concerns, roadblocks, and alternative solutions to colleagues of varying technical backgrounds has been invaluable in my professional development.</p><p style=\"margin-top: 0.875rem; margin-bottom: 0.875rem; padding: 0px; font-family: Publico, Times, &quot;Times New Roman&quot;, serif; color: rgb(34, 34, 34); font-size: 17px;\">Over the course of my career as a software engineer I have:</p><ul style=\"margin-top: 1.25rem; margin-bottom: 1.25rem; padding: 0px 1.5em 0px 0px; list-style: none; position: relative; z-index: 1; left: 1.5em; color: rgb(34, 34, 34); font-family: Publico, Times, &quot;Times New Roman&quot;, serif; font-size: 17px;\"><li style=\"margin: 0px 0px 0.625rem; padding: 0px; list-style-type: none;\">Become a certified Software Development Associate and Professional.</li><li style=\"margin: 0px 0px 0.625rem; padding: 0px; list-style-type: none;\">Led two successful software releases as the Java, team leader.</li><li style=\"margin: 0px 0px 0.625rem; padding: 0px; list-style-type: none;\">Increased team efficiency by implementing Agile methodologies.</li></ul><p style=\"margin-top: 0.875rem; margin-bottom: 0.875rem; padding: 0px; font-family: Publico, Times, &quot;Times New Roman&quot;, serif; color: rgb(34, 34, 34); font-size: 17px;\">I truly believe in continued education and research and continue to seek new software and methods to assist with product development. I hope to bring my knowledge, and future knowledge, to your organization.</p><p style=\"margin-top: 0.875rem; margin-bottom: 0.875rem; padding: 0px; font-family: Publico, Times, &quot;Times New Roman&quot;, serif; color: rgb(34, 34, 34); font-size: 17px;\">Attached is a copy of my resume that further explains my background and technical skills.</p><div id=\"native-placeholder_1-0\" class=\"comp native-placeholder mntl-block\" style=\"margin: 0px; padding: 0px; color: rgb(34, 34, 34); font-family: Publico, Times, &quot;Times New Roman&quot;, serif; font-size: 17px;\"></div><p class=\"cb-split\" style=\"margin-top: 0.875rem; margin-bottom: 0.875rem; padding: 0px; font-family: Publico, Times, &quot;Times New Roman&quot;, serif; color: rgb(34, 34, 34); font-size: 17px;\">I can be reached anytime via my cell phone, 555-555-5555 or via email at name@email.com. Thank you for your time and consideration. I look forward to speaking with you more about this opportunity.</p>'),(4,'Jimmy','Page','jimmy','bianchi@gmail.com','1992-07-13','bianchi','Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur.','Front End Developer',NULL,NULL,NULL,NULL),(24,'John Paul','Jones','user','user@user.com',NULL,'user','<p>One of the founding members of the legendary group Led Zeppelin, John Paul Jones started life on January 3, 1946, in London, England. In the turbulent years since then, he has left his mark on rock &amp;amp; roll music history as an innovative musician, arranger, and director.</p>','Musician',NULL,NULL,NULL,'<p>One of the founding members of the legendary group Led Zeppelin, John Paul Jones started life on January 3, 1946, in London, England. In the turbulent years since then, he has left his mark on rock &amp;amp; roll music history as an innovative musician, arranger, and director.</p><p><br></p><p>In 1960, when Jones was only 14 years old, he became a member of his father\'s dance band. This time under his father\'s watchful eye gave him a chance to gain experience and confidence. A year later Jones formed his first band, and by the next year he began to travel and perform professionally, at an age when school alone can apply too much pressure on a teen. By the mid-\'60s he had served for other groups as director and arranger, as well as bassist and keyboardist. His remarkable credits from that early period include artists like the Rolling Stones, the Outlaws, Jeff Beck, Mickey Most, the Yardbirds, the Mindbenders, the Everly Brothers, and the Supremes.</p><p><br></p><p>In 1968 Jones was chosen by Jimmy Page to help put together a new group that would be known as Led Zeppelin. In less than a dozen years, Jones, as a member of Zeppelin, made one movie, recorded nine full-length albums, that gave the world of rock &amp;amp; roll something to think over -- and completed close to 30 exhausting tours.</p><p><br></p><p>Zooma When Led Zeppelin ended in 1980, Jones continued his musical career. He began to produce and arrange albums for other artists; he also wrote songs, and then film scores for movies likes Scream for Help, Risk, and The Secret Adventures of Tom Thumb. In 1996 Jones saw the construction of his own studio completed. Two years later he finally recorded a solo debut album, Zooma.</p><p>During the 2000s he kept busy in both studio and live settings -- producing many artists and appearing on tracks by Foo Fighters, as well as jamming at Bonnaroo with Ben Harper and the Roots\' drummer ?uestlove. He also participated in the 2007 Led Zeppelin reunion show at O2 Arena, and in 2009 began playing with Dave Grohl (Foo Fighters) and Josh Homme (Queens of the Stone Age) in the supergroup Them Crooked Vultures. The band released its debut album later that year.</p>'),(30,'User 1','Surname','user1','us@u.com',NULL,'user1','<p><span style=\"color: rgb(51, 51, 51); font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif; font-size: 14px; text-align: justify;\">Angus McKinnon Young is a Scottish born Australian guitarist, songwriter and co-founder of the Australian hard rock band, AC/DC. His family migrated to Australia where he studied at Ashfield Boys High School. He dropped out of school at the age of 15. After playing in the band Kantuckee, later called Tantrum, he and his brother Malcolm formed the band AC/DC. He chose the school uniform as his signature look. Their first single was ?Can I Sit Next To You?, and their debut album was ?High Voltage?. Their studio album ?Highway to Hell? became their best-selling album. It was overtaken by the album ?Back in Black?, released as a tribute to Bon Scott, the band?s lead singer who died from alcohol poisoning. It became the second highest-selling album worldwide. The album ?For Those About to Rock We Salute You? was a big success. The band?s glory was reinstated with the studio album ?The Razors Edge?. With the album ?Black Ice?, AC/DC finally won a Grammy for the track ?War Machine?, after seven nominations. AC/DC were also inducted into the Rock and Roll Hall of Fame.</span><br></p>','Guitarrist',NULL,NULL,NULL,'<p><span style=\"color: rgb(51, 51, 51); font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif; font-size: 14px; text-align: justify;\">Angus McKinnon Young is a Scottish born Australian guitarist, songwriter and co-founder of the Australian hard rock band, AC/DC. His family migrated to Australia where he studied at Ashfield Boys High School. He dropped out of school at the age of 15. After playing in the band Kantuckee, later called Tantrum, he and his brother Malcolm formed the band AC/DC. He chose the school uniform as his signature look. Their first single was ?Can I Sit Next To You?, and their debut album was ?High Voltage?. Their studio album ?Highway to Hell? became their best-selling album. It was overtaken by the album ?Back in Black?, released as a tribute to Bon Scott, the band?s lead singer who died from alcohol poisoning. It became the second highest-selling album worldwide. The album ?For Those About to Rock We Salute You? was a big success. The band?s glory was reinstated with the studio album ?The Razors Edge?. With the album ?Black Ice?, AC/DC finally won a Grammy for the track ?War Machine?, after seven nominations. AC/DC were also inducted into the Rock and Roll Hall of Fame.</span><br></p>'),(32,'James','Blake','james','james@b.com',NULL,'james','<p><font face=\"Helvetica Neue\"><span style=\"color: rgb(51, 51, 51); text-align: justify;\">Siamo verso la fine del&nbsp;</span><a href=\"http://www.ondarock.it/ADMINOR/http:/www.ondarock.it/speciali/abc_decenniozero.htm\" style=\"margin: 0px; padding: 0px; color: rgb(17, 125, 210); text-align: justify; background-color: rgb(255, 255, 255);\">decennio Zero</a><span style=\"color: rgb(51, 51, 51); text-align: justify;\">&nbsp;e James Blake è un ragazzino di appena vent?anni a cui piace piazzare musica nelle infuocate serate del Mass Club o dell?ancor più osannato FWD di Londra. La metropoli inglese è la solita fucina di talenti, ma il timido giovanotto sembra non avere alcun interesse in produzioni ex novo. E? giovane, giovanissimo, e far muovere i fianchi ai suoi coetanei pare al momento l\'unico vero scopo. Conclusi gli studi di musica popolare alla Goldsmiths University di Londra, Blake divide il suo tempo tra i club, i negozi di dischi e la propria stanzetta, luogo ideale in cui assecondare le prime tentazioni produttive senza subire il peso di un qualche giudizio. Svincolatosi dunque dall?ambiente esterno, il piccolo e introverso dj londinese prova lentamente a sovrapporre sezioni ritmiche alla propria voce. Essere poi il figlio di James Litherland, chitarrista dei mitici Bandit e solista dal timbro a dir poco delicato ma purtroppo mai pienamente compreso nell?unico album ?4th Estate?, è una piccola benedizione.&nbsp;</span></font><br></p>','Musician',NULL,NULL,NULL,'<p><font face=\"Helvetica Neue\"><span style=\"color: rgb(51, 51, 51); text-align: justify;\">Siamo verso la fine del&nbsp;</span><a href=\"http://www.ondarock.it/ADMINOR/http:/www.ondarock.it/speciali/abc_decenniozero.htm\" style=\"margin: 0px; padding: 0px; color: rgb(17, 125, 210); text-align: justify; background-color: rgb(255, 255, 255);\">decennio Zero</a><span style=\"color: rgb(51, 51, 51); text-align: justify;\">&nbsp;e James Blake è un ragazzino di appena vent?anni a cui piace piazzare musica nelle infuocate serate del Mass Club o dell?ancor più osannato FWD di Londra. La metropoli inglese è la solita fucina di talenti, ma il timido giovanotto sembra non avere alcun interesse in produzioni ex novo. E? giovane, giovanissimo, e far muovere i fianchi ai suoi coetanei pare al momento l\'unico vero scopo. Conclusi gli studi di musica popolare alla Goldsmiths University di Londra, Blake divide il suo tempo tra i club, i negozi di dischi e la propria stanzetta, luogo ideale in cui assecondare le prime tentazioni produttive senza subire il peso di un qualche giudizio. Svincolatosi dunque dall?ambiente esterno, il piccolo e introverso dj londinese prova lentamente a sovrapporre sezioni ritmiche alla propria voce. Essere poi il figlio di James Litherland, chitarrista dei mitici Bandit e solista dal timbro a dir poco delicato ma purtroppo mai pienamente compreso nell?unico album ?4th Estate?, è una piccola benedizione.&nbsp;</span></font><br></p>'),(33,'tINI','gunter','userr','a@g.c',NULL,'userr','<p><a href=\"http://www.biography.com/people/john-lennon-9379045\" style=\"transition-duration: 0.25s; transition-timing-function: ease; background-color: rgb(255, 255, 255); -webkit-tap-highlight-color: transparent; outline: 0px; color: rgb(160, 0, 0); text-shadow: rgb(255, 255, 255) -2px 0px, rgb(255, 255, 255) -1px 0px, rgb(255, 255, 255) 1px 0px, rgb(255, 255, 255) 2px 0px; background-image: linear-gradient(transparent 49%, rgb(160, 0, 0) 50%); background-repeat: repeat-x; background-size: 1px 2px; background-position: 0px 90%; line-height: 1.6em; font-family: open-sans, sans-serif; font-size: 18px;\">John Lennon</a><span style=\"color: rgb(51, 51, 51); font-family: open-sans, sans-serif; font-size: 18px;\">&nbsp;would have turned 77 on October 9th. Of course, there have been multitudes of books written about his musical contribution to the world, both as one of The Beatles and as a solo artist. This particular writer has read quite a few of them, and couldn?t agree more that the music world?and perhaps the world as we know it?wouldn?t have been the same without him.</span><br></p>','Audio Video Designer',NULL,NULL,NULL,'<p><a href=\"http://www.biography.com/people/john-lennon-9379045\" style=\"transition-duration: 0.25s; transition-timing-function: ease; background-color: rgb(255, 255, 255); -webkit-tap-highlight-color: transparent; outline: 0px; color: rgb(160, 0, 0); text-shadow: rgb(255, 255, 255) -2px 0px, rgb(255, 255, 255) -1px 0px, rgb(255, 255, 255) 1px 0px, rgb(255, 255, 255) 2px 0px; background-image: linear-gradient(transparent 49%, rgb(160, 0, 0) 50%); background-repeat: repeat-x; background-size: 1px 2px; background-position: 0px 90%; line-height: 1.6em; font-family: open-sans, sans-serif; font-size: 18px;\">John Lennon</a><span style=\"color: rgb(51, 51, 51); font-family: open-sans, sans-serif; font-size: 18px;\">&nbsp;would have turned 77 on October 9th. Of course, there have been multitudes of books written about his musical contribution to the world, both as one of The Beatles and as a solo artist. This particular writer has read quite a few of them, and couldn?t agree more that the music world?and perhaps the world as we know it?wouldn?t have been the same without him.</span><br></p>');
/*!40000 ALTER TABLE `developer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `files` (
  `name` varchar(255) NOT NULL,
  `size` int(11) NOT NULL,
  `localfile` varchar(255) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `digest` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `private` tinyint(1) DEFAULT '0',
  `text` text,
  `type` varchar(45) DEFAULT 'commento',
  `project_ID` int(10) DEFAULT NULL,
  `developer_ID` int(10) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_project_idx` (`project_ID`),
  KEY `fk_dev_idx` (`developer_ID`),
  CONSTRAINT `fk_dev` FOREIGN KEY (`developer_ID`) REFERENCES `developer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_project` FOREIGN KEY (`project_ID`) REFERENCES `project` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,1,'bla bla bla','Discussion',14,3,'2017-11-05'),(2,0,'bla bla bla','Discussion',14,4,'2017-11-05'),(16,0,'sono james blake','Ad',14,32,'2017-10-23');
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` text,
  `location` varchar(100) DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  `picture` varchar(60) DEFAULT NULL,
  `coordinator_ID` int(10) NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_project_developer_idx` (`coordinator_ID`),
  CONSTRAINT `fk_project_developer` FOREIGN KEY (`coordinator_ID`) REFERENCES `developer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (13,'Progetto Semplice','<p><span style=\"color: rgb(115, 115, 115);\">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur.</span><br></p>',NULL,NULL,NULL,4,NULL),(14,'MDE Forge','<h3 style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.5em; margin-top: 0px; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68);\">MDE Forge is an extensible Web-based modeling platform specifically conceived to foster a community-based modeling&nbsp; repository, which underpins the development, analysis and reuse of modeling artifacts.</h3><h4 style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.4em; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68);\">Moreover, it enables the adoption of model management tools as software-as-a-service that can be remotely used without overwhelming the users with intricate and error-prone installation and configuration procedures.</h4><hr style=\"border-top: 0px; height: 1px; margin-bottom: 1.5em; color: rgb(102, 102, 102); font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 13px;\"><p style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 13px; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(102, 102, 102);\"><img class=\"aligncenter\" src=\"http://www.mdeforge.org/wp-content/uploads/2017/03/architecture.png\" alt=\"MDE Forge Platform\" style=\"clear: both; display: block; margin: 0px auto;\"></p><div class=\"lead center innerTB\" style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 13px; margin: 0px; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(102, 102, 102);\"><h3 class=\"header-h main-title text-black center\" style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.5em; font-style: inherit; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68); text-align: center;\">MDE Forge Platform</h3><h4 class=\"header-h main-text center\" style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.4em; font-style: inherit; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68); text-align: center;\">MDE Forge platform consists of a number of services that can be used by means of both a Web access and programmatic interfaces (API) that enable their adoption as software as a service.</h4><h4 class=\"header-h main-text text-black center\" style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.4em; font-style: inherit; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68); text-align: center;\">In particular, core services are provided to enable the management of modeling artifacts, namely transformations, models, metamodels, and editors.</h4><h4 class=\"header-h main-text center\" style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.4em; font-style: inherit; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68); text-align: center;\">Atop of such core services, extensions can be developed to add new functionalities.</h4></div><hr style=\"border-top: 0px; height: 1px; margin-bottom: 1.5em; color: rgb(102, 102, 102); font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 13px;\"><p style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 13px; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(102, 102, 102);\"><img class=\"logo-banner aligncenter\" src=\"http://www.mdeforge.org/wp-content/uploads/2017/03/logo-small-1.png\" style=\"clear: both; display: block; margin: 0px auto;\"></p><h3 class=\"center\" style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.5em; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68); text-align: center;\">MDE Forge has been design for:</h3><h4 style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.4em; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68);\">Developers of modeling artifacts</h4><p style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 13px; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(102, 102, 102);\">As previously said we envision a community of users that might want to share their tools and enable their adoption and refinement by other users. To this end the platform provides the means to add new modeling artifacts to the MDEForge repository.</p><h4 style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.4em; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68);\">Developers of MDEForge extensions</h4><p style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 13px; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(102, 102, 102);\">One of the requirements we identified when we started the development of MDEForge is about the modularity and extensibility of the platform. To this end we identified a set of core services that can be used to add new functionalities by means of platform extensions. In this respect, experienced users might contribute by proposing new extensions to be included in the platform.</p><h4 style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.4em; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68);\">End Users</h4><p style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 13px; margin-top: 1em; margin-bottom: 0px; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(102, 102, 102);\">A Web application enables end-users to search and use (meta)models, transformations, and editors available in the MDEForge repository. Experienced users might use the REST API to exploit the functionalities provided by the platform in a programmatic way. For instance, tool vendors might exploit the functionalities provided by their tools by exploiting some of the transformations available in the MDEForge repository.</p>','L\'aquila','Università degli Studi dell\'Aquila',NULL,4,'Model Driven Engineering'),(15,'LOUD','<h4 class=\"centered subtitle\" style=\"text-align: left; outline: none; font-family: Raleway, sans-serif; line-height: 1.2em; color: rgba(48, 66, 82, 0.7); margin-bottom: 0px; font-size: 26px; padding: 0px 0px 20px;\">LOUD is the best way to use Soundcloud</h4><h4 class=\"centered subtitle\" style=\"outline: none; font-family: Raleway, sans-serif; line-height: 1.2em; color: rgba(48, 66, 82, 0.7); margin-bottom: 0px; font-size: 26px; padding: 0px 0px 20px;\"><p style=\"text-align: left; outline: none; margin-bottom: 10px; color: black; font-size: 21px; line-height: 1.7em;\"><span style=\"outline: none; font-weight: 600;\">SoundCloud</span>&nbsp;is the most famous platform to share music. All tracks are available for free and you can have a better experience with them by using a less social context service. LOUD is focused more on&nbsp;<span style=\"outline: none; font-weight: 600;\">music context</span>, offering new features to organize your SoundCloud music like a real&nbsp;<span style=\"outline: none; font-weight: 600;\">music library</span>, in an easy and beautiful way.</p><hr class=\"no-color\" style=\"text-align: left; outline: none; margin-top: 40px; margin-bottom: 40px; border-width: 2px; border-right-style: initial; border-bottom-style: initial; border-left-style: initial; border-color: transparent; border-top-style: solid; width: 1170px; padding: 0px; color: rgb(51, 51, 51); font-size: 14px;\"><div class=\"row\" style=\"outline: none; color: rgb(51, 51, 51); font-size: 14px; text-align: center;\"><div class=\"col-sm-12\" style=\"outline: none; width: 1200px;\"><div style=\"text-align: center;\">&nbsp;&nbsp;&nbsp;<img width=\"200\" src=\"http://bianchii.herokuapp.com/projects/loud/icon.jpg\" style=\"outline: none; border-width: initial; border-style: none; float: left;\" class=\"note-float-left\"></div><p style=\"outline: none; margin-bottom: 10px; color: black; font-size: 21px; line-height: 1.7em;\"></p><div style=\"text-align: left;\"><img src=\"https://developers.soundcloud.com/assets/powered_by_black-4339b4c3c9cf88da9bfb15a16c4f6914.png\" style=\"outline: none; border-width: initial; border-style: none;\"></div></div></div><hr class=\"no-color\" style=\"text-align: left; outline: none; margin-top: 40px; margin-bottom: 40px; border-width: 2px; border-right-style: initial; border-bottom-style: initial; border-left-style: initial; border-color: transparent; border-top-style: solid; width: 1170px; padding: 0px; color: rgb(51, 51, 51); font-size: 14px;\"><p style=\"text-align: left; outline: none; margin-bottom: 10px; color: black; font-size: 21px; line-height: 1.7em;\">Listening to single tracks and long tracks together can be annoying, so LOUD allows you to manage your favorite podcast in a specific library, dividing them by Radio Stations. In your profile, Likes are orginized and divided by Users, and you can show your Followings, ordering them even by name.</p></h4>','L\'aquila','Giuseppe Bianchi',NULL,2,'Apache Cordova App');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill`
--

DROP TABLE IF EXISTS `skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skill` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type_ID` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_skill_type_idx` (`type_ID`),
  CONSTRAINT `fk_skill_type` FOREIGN KEY (`type_ID`) REFERENCES `type` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill`
--

LOCK TABLES `skill` WRITE;
/*!40000 ALTER TABLE `skill` DISABLE KEYS */;
INSERT INTO `skill` VALUES (3,'Java',2),(4,'C',1),(5,'UX Design',2),(6,'UI Design',1),(7,'XHTML',1),(8,'HTML',1),(9,'XML',1),(10,'HTML5',1),(11,'Adobe Illustrator',1),(12,'Adobe InDesign',1),(13,'Adobe Photoshop',1),(14,'Android Development',1),(15,'Art Design',1),(16,'AutoCAD',1),(17,'C++',1),(18,'MySql',1),(19,'CSS',1),(20,'Data Analytics',1),(21,'Fortran',1),(22,'iOS Development',1),(23,'XCode',1),(24,'Swift',1),(25,'Linux',1),(26,'Git',1),(27,'Javascript',1),(28,'Maya',1),(29,'J2EE',1),(30,'SEO',1),(31,'PHP',1),(32,'Perl',1),(33,'Phyton',1),(34,'Ruby',1),(35,'SQL',1),(36,'MongoDB',1),(37,'NoSQL',1),(38,'CSS3',1),(39,'React',1),(40,'Backbone',1),(41,'AngularJS',1),(42,'Apache Cordova',1),(43,'Apache',1),(44,'addd',3),(45,'addd',3),(46,'addd',3),(47,'eeee',14),(48,'eeee',14),(49,'eeee',14),(50,'ciao',3),(51,'ciao',3),(52,'ci',3),(53,'nuova',2),(54,'Develop',14),(55,'nuova',4),(56,'Prova',6);
/*!40000 ALTER TABLE `skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill_has_developer`
--

DROP TABLE IF EXISTS `skill_has_developer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skill_has_developer` (
  `skill_ID` int(10) NOT NULL,
  `developer_ID` int(10) NOT NULL,
  `level` int(11) DEFAULT '0',
  PRIMARY KEY (`skill_ID`,`developer_ID`),
  KEY `fk_skill_has_developer_developer1_idx` (`developer_ID`),
  KEY `fk_skill_has_developer_skill1_idx` (`skill_ID`),
  CONSTRAINT `fk_skill_has_developer_developer1` FOREIGN KEY (`developer_ID`) REFERENCES `developer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_skill_has_developer_skill1` FOREIGN KEY (`skill_ID`) REFERENCES `skill` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill_has_developer`
--

LOCK TABLES `skill_has_developer` WRITE;
/*!40000 ALTER TABLE `skill_has_developer` DISABLE KEYS */;
INSERT INTO `skill_has_developer` VALUES (3,2,10),(4,2,10),(5,2,10),(6,3,6),(6,24,3),(6,33,5),(7,24,6),(7,30,5),(7,32,6),(7,33,5),(11,32,4),(14,30,6),(18,3,8),(27,3,10);
/*!40000 ALTER TABLE `skill_has_developer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `numCollaborators` int(11) DEFAULT '1',
  `start` date DEFAULT NULL,
  `end` date DEFAULT NULL,
  `description` text,
  `open` tinyint(1) DEFAULT '1',
  `completed` tinyint(1) DEFAULT NULL,
  `project_ID` int(10) DEFAULT NULL,
  `type_ID` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_task_project1_idx` (`project_ID`),
  KEY `fk_tak_type_idx` (`type_ID`),
  CONSTRAINT `fk_tak_type` FOREIGN KEY (`type_ID`) REFERENCES `type` (`ID`),
  CONSTRAINT `fk_task_project1` FOREIGN KEY (`project_ID`) REFERENCES `project` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (5,'Analisi',5,'2017-11-05','2017-11-21','<p><span style=\"color: rgb(115, 115, 115);\">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur.</span><br></p>',1,NULL,13,4),(6,'Cluster',5,'2017-11-05','2017-11-28','<h3 style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.5em; margin-top: 0px; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68);\">MDE Forge is an extensible Web-based modeling platform specifically conceived to foster a community-based modeling&nbsp; repository, which underpins the development, analysis and reuse of modeling artifacts.</h3><h4 style=\"border: 0px; font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; font-size: 1.4em; margin-top: 1em; margin-bottom: 1em; outline: 0px; padding: 0px; vertical-align: baseline; color: rgb(68, 68, 68);\">Moreover, it enables the adoption of model management tools as software-as-a-service that can be remotely used without overwhelming the users with intricate and error-prone installation and configuration procedures.</h4>',1,1,14,5),(7,'Back End Service',0,'2017-10-19','2017-11-19','<h4 class=\"centered subtitle\" style=\"text-align: left; outline: none; font-family: Raleway, sans-serif; line-height: 1.2em; color: rgba(48, 66, 82, 0.7); margin-bottom: 0px; font-size: 26px; padding: 0px 0px 20px;\">LOUD is the best way to use Soundcloud</h4><p style=\"text-align: left; outline: none; margin-bottom: 10px; font-family: Raleway, sans-serif; font-size: 21px; line-height: 1.7em;\"><span style=\"outline: none; font-weight: 600;\">SoundCloud</span>&nbsp;is the most famous platform to share music. All tracks are available for free and you can have a better experience with them by using a less social context service. LOUD is focused more on&nbsp;<span style=\"outline: none; font-weight: 600;\">music context</span>, offering new features to organize your SoundCloud music like a real&nbsp;<span style=\"outline: none; font-weight: 600;\">music library</span>, in an easy and beautiful way.</p><div class=\"row\" style=\"text-align: left; outline: none; color: rgb(51, 51, 51); font-family: Raleway, sans-serif; font-size: 14px;\"><br></div>',1,NULL,15,22),(8,'UI Design',2,'2017-10-31','2017-11-20','<h4 class=\"centered subtitle\" style=\"text-align: left; outline: none; font-family: Raleway, sans-serif; line-height: 1.2em; color: rgba(48, 66, 82, 0.7); margin-bottom: 0px; font-size: 26px; padding: 0px 0px 20px;\">Listening to single tracks and long tracks together can be annoying, so LOUD allows you to manage your favorite podcast in a specific library, dividing them by Radio Stations. In your profile, Likes are orginized and divided by Users, and you can show your Followings, ordering them even by name.<br></h4>',1,NULL,15,17),(12,'Task Nuovo',3,'2017-10-04','2017-10-25','<p><br></p>',1,NULL,15,4),(13,'Task 2',3,'2017-10-12','2017-10-26','<p>fasgdsfgsdfgsdfgds</p>',1,NULL,15,6),(14,'UI Design',2,'2017-10-13','2017-10-25','<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur.<br></p>',1,1,14,21),(15,'Task Prova',4,'2017-10-20','2017-10-26','<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur.<br></p>',1,NULL,14,5),(16,'Analysis',6,'2017-10-16','2017-10-28','<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur. Lorem ipsum dolor sit amet, consectetur adipisicing elit. Proin nibh augue conseqaut nibbhi ellit ipsum consectetur.<br></p>',1,NULL,14,6);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_has_developer`
--

DROP TABLE IF EXISTS `task_has_developer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_has_developer` (
  `task_ID` int(10) NOT NULL,
  `developer_ID` int(10) NOT NULL,
  `state` int(11) DEFAULT '0',
  `date` date DEFAULT NULL,
  `vote` int(11) DEFAULT '0',
  `sender` int(11) DEFAULT NULL,
  PRIMARY KEY (`task_ID`,`developer_ID`),
  KEY `fk_task_has_developer_developer1_idx` (`developer_ID`),
  KEY `fk_task_has_developer_task1_idx` (`task_ID`),
  KEY `fk_sender_idx` (`sender`),
  CONSTRAINT `fk_sender` FOREIGN KEY (`sender`) REFERENCES `developer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_has_developer_developer1` FOREIGN KEY (`developer_ID`) REFERENCES `developer` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_has_developer_task1` FOREIGN KEY (`task_ID`) REFERENCES `task` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_has_developer`
--

LOCK TABLES `task_has_developer` WRITE;
/*!40000 ALTER TABLE `task_has_developer` DISABLE KEYS */;
INSERT INTO `task_has_developer` VALUES (5,1,1,'2017-11-05',2,1),(6,1,1,'2017-11-05',3,1),(6,2,-1,'2017-11-05',0,3),(6,3,1,'2017-11-05',0,4),(6,24,1,'2017-10-18',0,24),(7,4,1,'2017-10-18',0,3),(8,1,0,'2017-11-05',0,3),(14,3,2,'2017-10-17',5,4),(15,3,-1,'2017-10-18',0,2);
/*!40000 ALTER TABLE `task_has_developer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_has_skill`
--

DROP TABLE IF EXISTS `task_has_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_has_skill` (
  `task_ID` int(10) NOT NULL,
  `skill_ID` int(10) NOT NULL,
  `level_min` int(11) DEFAULT '0',
  PRIMARY KEY (`task_ID`,`skill_ID`),
  KEY `fk_task_has_skill_skill1_idx` (`skill_ID`),
  KEY `fk_task_has_skill_task1_idx` (`task_ID`),
  CONSTRAINT `fk_task_has_skill_skill1` FOREIGN KEY (`skill_ID`) REFERENCES `skill` (`ID`),
  CONSTRAINT `fk_task_has_skill_task1` FOREIGN KEY (`task_ID`) REFERENCES `task` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_has_skill`
--

LOCK TABLES `task_has_skill` WRITE;
/*!40000 ALTER TABLE `task_has_skill` DISABLE KEYS */;
INSERT INTO `task_has_skill` VALUES (5,3,1),(5,5,1),(6,3,3),(6,18,5),(6,25,3),(7,6,3),(7,7,9),(8,8,9),(8,19,6),(8,27,9),(8,42,6),(13,5,8),(13,7,4),(14,5,5),(14,6,5),(14,10,5),(14,30,6),(15,6,5),(15,7,5),(16,5,7),(16,7,5);
/*!40000 ALTER TABLE `task_has_skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type`
--

DROP TABLE IF EXISTS `type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `type` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type`
--

LOCK TABLES `type` WRITE;
/*!40000 ALTER TABLE `type` DISABLE KEYS */;
INSERT INTO `type` VALUES (1,'Business Analysis'),(2,'Business Intelligence'),(3,'Business Storytelling'),(4,'Cloud Computing'),(5,'Data Analysis'),(6,'Data Warehousing'),(7,'Database Administration'),(8,'Database Management'),(9,'Editing'),(10,'Game Development'),(11,'Information Management'),(12,'Information Security'),(13,'Software Engineering'),(14,'Web Development'),(15,'Software Development'),(16,'Web Engineering'),(17,'UI / UX'),(18,'User Testing'),(19,'Mobile Development'),(20,'Mobile App Development'),(21,'Front End Development'),(22,'Back End Development'),(23,'Nuovo Type');
/*!40000 ALTER TABLE `type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-24  9:45:47

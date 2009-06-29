-- phpMyAdmin SQL Dump
-- version 3.1.3
-- http://www.phpmyadmin.net
--
-- Gazda: localhost
-- Timp de generare: 30 Iun 2009 la 01:50
-- Versiune server: 5.1.32
-- Versiune PHP: 5.2.9-1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Baza de date: `topchatserver`
--
CREATE DATABASE `topchatserver` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `topchatserver`;

-- --------------------------------------------------------

--
-- Structura de tabel pentru tabelul `messages`
--

CREATE TABLE IF NOT EXISTS `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(10) NOT NULL,
  `message` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=155 ;

--
-- Salvarea datelor din tabel `messages`
--

INSERT INTO `messages` (`id`, `type`, `message`, `timestamp`) VALUES
(1, 'receive', '<stream:stream to="localhost" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-28 12:01:06'),
(2, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-28 12:01:06'),
(3, 'sent', '<stream:features><starttls xmlns="urn:ietf:params:xml:ns:xmpp-tls"><required></required></starttls></stream:features>', '2009-06-28 12:01:06'),
(4, 'receive', '<starttls xmlns="urn:ietf:params:xml:ns:xmpp-tls"/>', '2009-06-28 12:01:06'),
(5, 'sent', '<proceed xmlns="urn:ietf:params:xml:ns:xmpp-tls"></proceed>', '2009-06-28 12:01:06'),
(6, 'receive', '<stream:stream to="example.com" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-28 12:01:07'),
(7, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-28 12:01:07'),
(8, 'sent', '<stream:features><mechanisms xmlns="urn:ietf:params:xml:ns:xmpp-sasl"><mechanism>PLAIN</mechanism></mechanisms></stream:features>', '2009-06-28 12:01:07'),
(9, 'receive', '<auth mechanism="PLAIN" xmlns="urn:ietf:params:xml:ns:xmpp-sasl">b2FuYQBvYW5hAGFnYXRoYQ==</auth>', '2009-06-28 12:01:07'),
(10, 'receive', '<stream:stream to="example.com" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-28 12:01:07'),
(11, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-28 12:01:07'),
(12, 'sent', '<stream:features><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"></bind><session xmlns="urn:ietf:params:xml:ns:xmpp-session"></session></stream:features>', '2009-06-28 12:01:07'),
(13, 'receive', '<iq id="126MX-0" type="set"><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"><resource></resource></bind></iq>', '2009-06-28 12:01:07'),
(14, 'receive', '<iq id="126MX-1" type="set"><session xmlns="urn:ietf:params:xml:ns:xmpp-session"/></iq>', '2009-06-28 12:01:07'),
(15, 'receive', '<iq id="126MX-2" type="get"><query xmlns="jabber:iq:roster"></query></iq>', '2009-06-28 12:01:07'),
(16, 'receive', '<presence id="126MX-3"></presence>', '2009-06-28 12:01:07'),
(17, 'receive', '<presence id="126MX-4" to="camera@conference.jabber.org/nick"><x xmlns="http://jabber.org/protocol/muc"></x></presence>', '2009-06-28 12:01:34'),
(18, 'receive', '<iq id="126MX-5" to="camera@conference.jabber.org" type="set"><query xmlns="http://jabber.org/protocol/muc#owner"><x xmlns="jabber:x:data" type="submit"></x></query></iq>', '2009-06-28 12:01:35'),
(19, 'receive', '<presence id="126MX-6" to="camera@conference.jabber.org/nick" type="unavailable"></presence><presence id="126MX-7" to="camera@conference.jabber.org/nick"><x xmlns="http://jabber.org/protocol/muc"></x></presence>', '2009-06-28 12:01:35'),
(20, 'receive', '<message id="126MX-8" to="camera@conference.jabber.org" type="groupchat"><body>aaaaa</body></message>', '2009-06-28 12:01:36'),
(21, 'receive', '<message id="126MX-9" to="camera@conference.jabber.org" type="groupchat"><body>eee</body></message>', '2009-06-28 12:01:38'),
(22, 'receive', '<stream:stream to="localhost" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-28 22:56:11'),
(23, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-28 22:56:11'),
(24, 'sent', '<stream:features><starttls xmlns="urn:ietf:params:xml:ns:xmpp-tls"><required></required></starttls></stream:features>', '2009-06-28 22:56:11'),
(25, 'receive', '<starttls xmlns="urn:ietf:params:xml:ns:xmpp-tls"/>', '2009-06-28 22:56:11'),
(26, 'sent', '<proceed xmlns="urn:ietf:params:xml:ns:xmpp-tls"></proceed>', '2009-06-28 22:56:11'),
(27, 'receive', '<stream:stream to="example.com" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-28 22:56:12'),
(28, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-28 22:56:12'),
(29, 'sent', '<stream:features><mechanisms xmlns="urn:ietf:params:xml:ns:xmpp-sasl"><mechanism>PLAIN</mechanism></mechanisms></stream:features>', '2009-06-28 22:56:12'),
(30, 'receive', '<auth mechanism="PLAIN" xmlns="urn:ietf:params:xml:ns:xmpp-sasl">b2FuYQBvYW5hAGFnYXRoYQ==</auth>', '2009-06-28 22:56:12'),
(31, 'receive', '<stream:stream to="example.com" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-28 22:56:12'),
(32, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-28 22:56:12'),
(33, 'sent', '<stream:features><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"></bind><session xmlns="urn:ietf:params:xml:ns:xmpp-session"></session></stream:features>', '2009-06-28 22:56:12'),
(34, 'receive', '<iq id="awLoX-0" type="set"><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"><resource></resource></bind></iq>', '2009-06-28 22:56:12'),
(35, 'receive', '<iq id="awLoX-1" type="set"><session xmlns="urn:ietf:params:xml:ns:xmpp-session"/></iq>', '2009-06-28 22:56:12'),
(36, 'receive', '<iq id="awLoX-2" type="get"><query xmlns="jabber:iq:roster"></query></iq>', '2009-06-28 22:56:12'),
(37, 'receive', '<presence id="awLoX-3"></presence>', '2009-06-28 22:56:12'),
(38, 'receive', '<presence id="awLoX-4" to="cem@conference.jabber.org/eueu"><x xmlns="http://jabber.org/protocol/muc"></x></presence>', '2009-06-28 22:56:19'),
(39, 'receive', '<iq id="awLoX-5" to="cem@conference.jabber.org" type="set"><query xmlns="http://jabber.org/protocol/muc#owner"><x xmlns="jabber:x:data" type="submit"></x></query></iq>', '2009-06-28 22:56:19'),
(40, 'receive', '<presence id="awLoX-6" to="cem@conference.jabber.org/eueu" type="unavailable"></presence><presence id="awLoX-7" to="cem@conference.jabber.org/eueu"><x xmlns="http://jabber.org/protocol/muc"></x></presence>', '2009-06-28 22:56:19'),
(41, 'sent', '<success xmlns=''urn:ietf:params:xml:ns:xmpp-sasl''/>', '2009-06-28 22:57:32'),
(42, 'sent', '<iq type=''result'' id=''lE7cL-0''><bind xmlns=''urn:ietf:params:xml:ns:xmpp-bind''><jid>oana@example.com/oana1</jid></bind></iq>', '2009-06-28 22:57:32'),
(43, 'sent', '<iq type=''result'' id=''lE7cL-1''></iq>', '2009-06-28 22:57:32'),
(44, 'receive', ' ', '2009-06-28 22:58:17'),
(45, 'receive', ' ', '2009-06-28 22:58:47'),
(46, 'receive', '<stream:stream to="localhost" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-28 22:59:01'),
(47, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-28 22:59:01'),
(48, 'sent', '<stream:features><starttls xmlns="urn:ietf:params:xml:ns:xmpp-tls"><required></required></starttls></stream:features>', '2009-06-28 22:59:01'),
(49, 'receive', '<starttls xmlns="urn:ietf:params:xml:ns:xmpp-tls"/>', '2009-06-28 22:59:01'),
(50, 'sent', '<proceed xmlns="urn:ietf:params:xml:ns:xmpp-tls"></proceed>', '2009-06-28 22:59:01'),
(51, 'receive', '<stream:stream to="example.com" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-28 22:59:01'),
(52, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-28 22:59:01'),
(53, 'sent', '<stream:features><mechanisms xmlns="urn:ietf:params:xml:ns:xmpp-sasl"><mechanism>PLAIN</mechanism></mechanisms></stream:features>', '2009-06-28 22:59:01'),
(54, 'receive', '<auth mechanism="PLAIN" xmlns="urn:ietf:params:xml:ns:xmpp-sasl">b2FuYQBvYW5hAGFnYXRoYQ==</auth>', '2009-06-28 22:59:01'),
(55, 'sent', '<success xmlns="urn:ietf:params:xml:ns:xmpp-sasl"/>', '2009-06-28 22:59:01'),
(56, 'receive', '<stream:stream to="example.com" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-28 22:59:01'),
(57, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-28 22:59:01'),
(58, 'sent', '<stream:features><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"></bind><session xmlns="urn:ietf:params:xml:ns:xmpp-session"></session></stream:features>', '2009-06-28 22:59:01'),
(59, 'receive', '<iq id="9hd2W-0" type="set"><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"><resource></resource></bind></iq>', '2009-06-28 22:59:01'),
(60, 'sent', '<iq type="result" id="9hd2W-0"><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"><jid>oana@example.com/oana1</jid></bind></iq>', '2009-06-28 22:59:01'),
(61, 'receive', '<iq id="9hd2W-1" type="set"><session xmlns="urn:ietf:params:xml:ns:xmpp-session"/></iq>', '2009-06-28 22:59:01'),
(62, 'sent', '<iq type="result" id="9hd2W-1"></iq>', '2009-06-28 22:59:01'),
(63, 'receive', '<iq id="9hd2W-2" type="get"><query xmlns="jabber:iq:roster"></query></iq>', '2009-06-28 22:59:01'),
(64, 'receive', '<presence id="9hd2W-3"></presence>', '2009-06-28 22:59:01'),
(65, 'receive', '<presence id="9hd2W-4" to="camer@conference.jabber.org/aaa"><x xmlns="http://jabber.org/protocol/muc"></x></presence>', '2009-06-28 22:59:10'),
(66, 'sent', '<presence id="9hd2W-4"to="oana@example.com/oana1"from="camer@conference.jabber.org/aaa"><status>null</status><x xmlns="http://jabber.org/protocol/muc#user"><item affiliation="owner" jid="oana@example.com/oana1"role="moderator"><reason></reason><actor jid=""/></item><status code="201" /></x></presence>', '2009-06-28 22:59:10'),
(67, 'receive', '<iq id="9hd2W-5" to="camer@conference.jabber.org" type="set"><query xmlns="http://jabber.org/protocol/muc#owner"><x xmlns="jabber:x:data" type="submit"></x></query></iq>', '2009-06-28 22:59:10'),
(68, 'sent', '<iq type="result" id="9hd2W-5"to="oana@example.com/oana1"from="camer@conference.jabber.org"><query xmlns="http://jabber.org/protocol/muc#owner"></query></iq>', '2009-06-28 22:59:10'),
(69, 'receive', '<presence id="9hd2W-6" to="camer@conference.jabber.org/aaa" type="unavailable"></presence><presence id="9hd2W-7" to="camer@conference.jabber.org/aaa"><x xmlns="http://jabber.org/protocol/muc"></x></presence>', '2009-06-28 22:59:10'),
(70, 'sent', '<presence id="a1"to="oana@example.com/oana1"from="camer@conference.jabber.org/aaa"><status>null</status><x xmlns="http://jabber.org/protocol/muc#user"><item affiliation="none" role="participant"><reason></reason><actor jid=""/></item></x></presence>', '2009-06-28 22:59:10'),
(71, 'sent', '<presence id="9hd2W-7"to="oana@example.com/oana1"from="camer@conference.jabber.org/aaa"><status>null</status><x xmlns="http://jabber.org/protocol/muc#user"><item affiliation="owner" jid="oana@example.com/oana1"role="moderator"><reason></reason><actor jid=""/></item></x></presence>', '2009-06-28 22:59:10'),
(72, 'receive', '<message id="9hd2W-8" to="camer@conference.jabber.org" type="groupchat"><body>wwwwwwwwwwwwwwwwwwwwwwwww</body></message>', '2009-06-28 22:59:19'),
(73, 'sent', '<message id="9hd2W-8" to="oana@example.com/oana1" from="camer@conference.jabber.org/aaa" type="groupchat"><body>wwwwwwwwwwwwwwwwwwwwwwwww</body></message>', '2009-06-28 22:59:19'),
(74, 'receive', '<message id="9hd2W-9" to="camer@conference.jabber.org" type="groupchat"><body>pac pac</body></message>', '2009-06-28 22:59:29'),
(75, 'sent', '<message id="9hd2W-9" to="oana@example.com/oana1" from="camer@conference.jabber.org/aaa" type="groupchat"><body>pac pac</body></message>', '2009-06-28 22:59:29'),
(76, 'receive', '<stream:stream to="localhost" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-30 00:13:54'),
(77, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-30 00:13:54'),
(78, 'sent', '<stream:features><starttls xmlns="urn:ietf:params:xml:ns:xmpp-tls"><required></required></starttls></stream:features>', '2009-06-30 00:13:54'),
(79, 'receive', '<starttls xmlns="urn:ietf:params:xml:ns:xmpp-tls"/>', '2009-06-30 00:13:54'),
(80, 'sent', '<proceed xmlns="urn:ietf:params:xml:ns:xmpp-tls"></proceed>', '2009-06-30 00:13:55'),
(81, 'receive', '<stream:stream to="example.com" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-30 00:13:55'),
(82, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-30 00:13:55'),
(83, 'sent', '<stream:features><mechanisms xmlns="urn:ietf:params:xml:ns:xmpp-sasl"><mechanism>PLAIN</mechanism></mechanisms></stream:features>', '2009-06-30 00:13:55'),
(84, 'receive', '<auth mechanism="PLAIN" xmlns="urn:ietf:params:xml:ns:xmpp-sasl">b2FuYQBvYW5hAGFnYXRoYQ==</auth>', '2009-06-30 00:13:55'),
(85, 'sent', '<success xmlns="urn:ietf:params:xml:ns:xmpp-sasl"/>', '2009-06-30 00:13:55'),
(86, 'receive', '<stream:stream to="example.com" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-30 00:13:55'),
(87, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-30 00:13:55'),
(88, 'sent', '<stream:features><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"></bind><session xmlns="urn:ietf:params:xml:ns:xmpp-session"></session></stream:features>', '2009-06-30 00:13:55'),
(89, 'receive', '<iq id="I7Fyh-0" type="set"><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"><resource></resource></bind></iq>', '2009-06-30 00:13:55'),
(90, 'sent', '<iq type="result" id="I7Fyh-0"><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"><jid>oana@example.com/oana1</jid></bind></iq>', '2009-06-30 00:13:55'),
(91, 'receive', '<iq id="I7Fyh-1" type="set"><session xmlns="urn:ietf:params:xml:ns:xmpp-session"/></iq>', '2009-06-30 00:13:55'),
(92, 'sent', '<iq type="result" id="I7Fyh-1"></iq>', '2009-06-30 00:13:55'),
(93, 'receive', '<iq id="I7Fyh-2" type="get"><query xmlns="jabber:iq:roster"></query></iq>', '2009-06-30 00:13:55'),
(94, 'receive', '<presence id="I7Fyh-3"></presence>', '2009-06-30 00:13:55'),
(95, 'receive', '<presence id="I7Fyh-4" to="ceee@conference.jabber.org/aa"><x xmlns="http://jabber.org/protocol/muc"></x></presence>', '2009-06-30 00:14:00'),
(96, 'sent', '<presence id="I7Fyh-4"to="oana@example.com/oana1"from="ceee@conference.jabber.org/aa"><status>null</status><x xmlns="http://jabber.org/protocol/muc#user"><item affiliation="owner" jid="oana@example.com/oana1"role="moderator"><reason></reason><actor jid=""/></item><status code="201" /></x></presence>', '2009-06-30 00:14:00'),
(97, 'receive', '<iq id="I7Fyh-5" to="ceee@conference.jabber.org" type="set"><query xmlns="http://jabber.org/protocol/muc#owner"><x xmlns="jabber:x:data" type="submit"></x></query></iq>', '2009-06-30 00:14:00'),
(98, 'sent', '<iq type="result" id="I7Fyh-5"to="oana@example.com/oana1"from="ceee@conference.jabber.org"><query xmlns="http://jabber.org/protocol/muc#owner"></query></iq>', '2009-06-30 00:14:00'),
(99, 'receive', '<presence id="I7Fyh-6" to="ceee@conference.jabber.org/aa" type="unavailable"></presence><presence id="I7Fyh-7" to="ceee@conference.jabber.org/aa"><x xmlns="http://jabber.org/protocol/muc"></x></presence>', '2009-06-30 00:14:00'),
(100, 'sent', '<presence id="a1"to="oana@example.com/oana1"from="ceee@conference.jabber.org/aa"><status>null</status><x xmlns="http://jabber.org/protocol/muc#user"><item affiliation="none" role="participant"><reason></reason><actor jid=""/></item></x></presence>', '2009-06-30 00:14:00'),
(101, 'sent', '<presence id="I7Fyh-7"to="oana@example.com/oana1"from="ceee@conference.jabber.org/aa"><status>null</status><x xmlns="http://jabber.org/protocol/muc#user"><item affiliation="owner" jid="oana@example.com/oana1"role="moderator"><reason></reason><actor jid=""/></item></x></presence>', '2009-06-30 00:14:00'),
(102, 'receive', '<message id="I7Fyh-8" to="ceee@conference.jabber.org" type="groupchat"><body>grigore</body></message>', '2009-06-30 00:14:04'),
(103, 'sent', '<message id="I7Fyh-8" to="oana@example.com/oana1" from="ceee@conference.jabber.org/aa" type="groupchat"><body>grigore</body></message>', '2009-06-30 00:14:04'),
(104, 'receive', '<stream:stream to="localhost" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-30 00:14:23'),
(105, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-30 00:14:23'),
(106, 'sent', '<stream:features><starttls xmlns="urn:ietf:params:xml:ns:xmpp-tls"><required></required></starttls></stream:features>', '2009-06-30 00:14:23'),
(107, 'receive', '<starttls xmlns="urn:ietf:params:xml:ns:xmpp-tls"/>', '2009-06-30 00:14:23'),
(108, 'sent', '<proceed xmlns="urn:ietf:params:xml:ns:xmpp-tls"></proceed>', '2009-06-30 00:14:23'),
(109, 'receive', '<stream:stream to="example.com" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-30 00:14:24'),
(110, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-30 00:14:24'),
(111, 'sent', '<stream:features><mechanisms xmlns="urn:ietf:params:xml:ns:xmpp-sasl"><mechanism>PLAIN</mechanism></mechanisms></stream:features>', '2009-06-30 00:14:24'),
(112, 'receive', '<auth mechanism="PLAIN" xmlns="urn:ietf:params:xml:ns:xmpp-sasl">bGF1cmEAbGF1cmEAcGFyb2xh</auth>', '2009-06-30 00:14:24'),
(113, 'sent', '<success xmlns="urn:ietf:params:xml:ns:xmpp-sasl"/>', '2009-06-30 00:14:24'),
(114, 'receive', '<stream:stream to="example.com" xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" version="1.0">', '2009-06-30 00:14:24'),
(115, 'sent', '<stream:stream xmlns="jabber:client" xmlns:stream="http://etherx.jabber.org/streams" from="example.com" id="someid" version="1.0">', '2009-06-30 00:14:24'),
(116, 'sent', '<stream:features><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"></bind><session xmlns="urn:ietf:params:xml:ns:xmpp-session"></session></stream:features>', '2009-06-30 00:14:24'),
(117, 'receive', '<iq id="O55Q7-0" type="set"><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"><resource></resource></bind></iq>', '2009-06-30 00:14:24'),
(118, 'sent', '<iq type="result" id="O55Q7-0"><bind xmlns="urn:ietf:params:xml:ns:xmpp-bind"><jid>laura@example.com/laura1</jid></bind></iq>', '2009-06-30 00:14:24'),
(119, 'receive', '<iq id="O55Q7-1" type="set"><session xmlns="urn:ietf:params:xml:ns:xmpp-session"/></iq>', '2009-06-30 00:14:24'),
(120, 'sent', '<iq type="result" id="O55Q7-1"></iq>', '2009-06-30 00:14:24'),
(121, 'receive', '<iq id="O55Q7-2" type="get"><query xmlns="jabber:iq:roster"></query></iq>', '2009-06-30 00:14:24'),
(122, 'receive', '<presence id="O55Q7-3"></presence>', '2009-06-30 00:14:24'),
(123, 'receive', '<presence id="O55Q7-4" to="ceee@conference.jabber.org/eee"><x xmlns="http://jabber.org/protocol/muc"></x></presence>', '2009-06-30 00:14:30'),
(124, 'sent', '<presence id="a1"to="oana@example.com/oana1"from="ceee@conference.jabber.org/eee"><status>null</status><x xmlns="http://jabber.org/protocol/muc#user"><item affiliation="none" role="participant"><reason></reason><actor jid=""/></item></x></presence>', '2009-06-30 00:14:30'),
(125, 'sent', '<presence id="a1"to="laura@example.com/laura1"from="ceee@conference.jabber.org/eee"><status>null</status><x xmlns="http://jabber.org/protocol/muc#user"><item affiliation="none" role="participant"><reason></reason><actor jid=""/></item></x></presence>', '2009-06-30 00:14:30'),
(126, 'sent', '<presence id="O55Q7-4"to="laura@example.com/laura1"from="ceee@conference.jabber.org/aa"><status>null</status><x xmlns="http://jabber.org/protocol/muc#user"><item affiliation="owner" jid="laura@example.com/laura1"role="moderator"><reason></reason><actor jid=""/></item></x></presence>', '2009-06-30 00:14:30'),
(127, 'sent', '<presence id="O55Q7-4"to="laura@example.com/laura1"from="ceee@conference.jabber.org/eee"><status>null</status><x xmlns="http://jabber.org/protocol/muc#user"><item affiliation="none" jid="laura@example.com/laura1"role="participant"><reason></reason><actor jid=""/></item></x></presence>', '2009-06-30 00:14:30'),
(128, 'receive', '<presence id="O55Q7-5" to="ceee@conference.jabber.org/eee"><status>www</status></presence>', '2009-06-30 00:14:30'),
(129, 'receive', '<message id="O55Q7-6" to="ceee@conference.jabber.org" type="groupchat"><body>ura uira</body></message><message id="O55Q7-7" to="ceee" type="groupchat"><body></body></message>', '2009-06-30 00:14:36'),
(130, 'sent', '<message id="O55Q7-6" to="oana@example.com/oana1" from="ceee@conference.jabber.org/eee" type="groupchat"><body>ura uira</body></message>', '2009-06-30 00:14:36'),
(131, 'sent', '<message id="O55Q7-6" to="laura@example.com/laura1" from="ceee@conference.jabber.org/eee" type="groupchat"><body>ura uira</body></message>', '2009-06-30 00:14:36'),
(132, 'receive', '<message id="O55Q7-8" to="ceee@conference.jabber.org" type="groupchat"><body>aaa</body></message><message id="O55Q7-9" to="ceee" type="groupchat"><body></body></message>', '2009-06-30 00:14:38'),
(133, 'sent', '<message id="O55Q7-8" to="oana@example.com/oana1" from="ceee@conference.jabber.org/eee" type="groupchat"><body>aaa</body></message>', '2009-06-30 00:14:38'),
(134, 'sent', '<message id="O55Q7-8" to="laura@example.com/laura1" from="ceee@conference.jabber.org/eee" type="groupchat"><body>aaa</body></message>', '2009-06-30 00:14:38'),
(135, 'receive', ' ', '2009-06-30 00:14:40'),
(136, 'receive', '<message id="O55Q7-10" to="ceee@conference.jabber.org" type="groupchat"><body>aaaaaaaaaaaa</body></message><message id="O55Q7-11" to="ceee" type="groupchat"><body></body></message>', '2009-06-30 00:14:41'),
(137, 'sent', '<message id="O55Q7-10" to="oana@example.com/oana1" from="ceee@conference.jabber.org/eee" type="groupchat"><body>aaaaaaaaaaaa</body></message>', '2009-06-30 00:14:41'),
(138, 'sent', '<message id="O55Q7-10" to="laura@example.com/laura1" from="ceee@conference.jabber.org/eee" type="groupchat"><body>aaaaaaaaaaaa</body></message>', '2009-06-30 00:14:41'),
(139, 'receive', '<message id="I7Fyh-9" to="ceee@conference.jabber.org" type="groupchat"><body>ui</body></message>', '2009-06-30 00:14:45'),
(140, 'sent', '<message id="I7Fyh-9" to="oana@example.com/oana1" from="ceee@conference.jabber.org/aa" type="groupchat"><body>ui</body></message>', '2009-06-30 00:14:45'),
(141, 'sent', '<message id="I7Fyh-9" to="laura@example.com/laura1" from="ceee@conference.jabber.org/aa" type="groupchat"><body>ui</body></message>', '2009-06-30 00:14:45'),
(142, 'receive', '<message id="I7Fyh-10" to="ceee@conference.jabber.org" type="groupchat"><body>tres bien</body></message>', '2009-06-30 00:14:52'),
(143, 'sent', '<message id="I7Fyh-10" to="oana@example.com/oana1" from="ceee@conference.jabber.org/aa" type="groupchat"><body>tres bien</body></message>', '2009-06-30 00:14:52'),
(144, 'sent', '<message id="I7Fyh-10" to="laura@example.com/laura1" from="ceee@conference.jabber.org/aa" type="groupchat"><body>tres bien</body></message>', '2009-06-30 00:14:52'),
(145, 'receive', '<message id="O55Q7-12" to="ceee@conference.jabber.org" type="groupchat"><body>matelot</body></message><message id="O55Q7-13" to="ceee" type="groupchat"><body></body></message>', '2009-06-30 00:15:00'),
(146, 'sent', '<message id="O55Q7-12" to="oana@example.com/oana1" from="ceee@conference.jabber.org/eee" type="groupchat"><body>matelot</body></message>', '2009-06-30 00:15:00'),
(147, 'sent', '<message id="O55Q7-12" to="laura@example.com/laura1" from="ceee@conference.jabber.org/eee" type="groupchat"><body>matelot</body></message>', '2009-06-30 00:15:00'),
(148, 'receive', '<message id="I7Fyh-11" to="ceee@conference.jabber.org" type="groupchat"><body>parfait</body></message>', '2009-06-30 00:15:08'),
(149, 'sent', '<message id="I7Fyh-11" to="oana@example.com/oana1" from="ceee@conference.jabber.org/aa" type="groupchat"><body>parfait</body></message>', '2009-06-30 00:15:08'),
(150, 'sent', '<message id="I7Fyh-11" to="laura@example.com/laura1" from="ceee@conference.jabber.org/aa" type="groupchat"><body>parfait</body></message>', '2009-06-30 00:15:08'),
(151, 'receive', '<message id="O55Q7-14" to="ceee@conference.jabber.org" type="groupchat"><body>talmes balmes</body></message><message id="O55Q7-15" to="ceee" type="groupchat"><body></body></message>', '2009-06-30 00:15:18'),
(152, 'sent', '<message id="O55Q7-14" to="oana@example.com/oana1" from="ceee@conference.jabber.org/eee" type="groupchat"><body>talmes balmes</body></message>', '2009-06-30 00:15:18'),
(153, 'sent', '<message id="O55Q7-14" to="laura@example.com/laura1" from="ceee@conference.jabber.org/eee" type="groupchat"><body>talmes balmes</body></message>', '2009-06-30 00:15:18'),
(154, 'sent', '<presence id="a1"to="oana@example.com/oana1"from="ceee@conference.jabber.org/eee"type="unavailable"><x xmlns="http://jabber.org/protocol/muc#user"><item affiliation="none" role="participant"><reason></reason><actor jid=""/></item></x></presence>', '2009-06-30 00:15:22');

-- --------------------------------------------------------

--
-- Structura de tabel pentru tabelul `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Salvarea datelor din tabel `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(1, 'grigore', 'parola'),
(2, 'ioana', 'parola'),
(3, 'oana', 'agatha'),
(4, 'laura', 'parola'),
(5, 'gigel', 'parola');

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3307
-- Tiempo de generación: 08-05-2024 a las 03:16:47
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `money transaction system`
--
CREATE DATABASE IF NOT EXISTS `money transaction system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `money transaction system`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `transaction`
--

CREATE TABLE `transaction` (
  `IDTransaction` int(9) NOT NULL,
  `IDSourceUser` int(9) UNSIGNED NOT NULL,
  `IDDestionationUser` int(9) UNSIGNED NOT NULL,
  `TotalAmount` double NOT NULL,
  `Currency` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `transaction`
--

INSERT INTO `transaction` (`IDTransaction`, `IDSourceUser`, `IDDestionationUser`, `TotalAmount`, `Currency`) VALUES
(1, 13724982, 524215324, 500.25, 'USD'),
(2, 21781927, 715254, 100.5, 'EUR'),
(3, 32413, 9345369, 300.75, 'GBP'),
(4, 35252332, 6432124, 700.3, 'CAD'),
(5, 6432124, 715254, 150.45, 'AUD'),
(6, 715254, 13724982, 200.6, 'JPY'),
(7, 9345369, 21781927, 50, 'CHF'),
(8, 1048921, 35252332, 80.2, 'CNY'),
(9, 13724982, 1048921, 120.9, 'KRW'),
(10, 21781927, 32413, 180.75, 'USD');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `IDUser` int(9) UNSIGNED NOT NULL,
  `Name` varchar(30) NOT NULL,
  `Last Name` varchar(30) NOT NULL,
  `Birthdate` date DEFAULT NULL,
  `Total Money` double DEFAULT NULL,
  `Email` varchar(84) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `Currency` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`IDUser`, `Name`, `Last Name`, `Birthdate`, `Total Money`, `Email`, `Password`, `Currency`) VALUES
(32413, 'Emily', 'Brown', '1995-11-25', 1500.3, 'emily@example.com', 'password', 'CAD'),
(715254, 'Ethan', 'Miller', '1980-09-12', 3000, 'ethan@example.com', 'qwerty', 'CHF'),
(1048921, 'Ava', 'Anderson', '1987-12-28', 1950.9, 'ava@example.com', 'test123', 'KRW'),
(6432124, 'Sophia', 'Davis', '1992-03-30', 2200.6, 'sophia@example.com', 'abc123', 'JPY'),
(9345369, 'Noah', 'Taylor', '1996-07-08', 1650.2, 'noah@example.com', 'mypassword', 'CNY'),
(13724982, 'John', 'Doe', '1990-01-15', 1000.5, 'john@example.com', 'password123', 'USD'),
(21781927, 'Alice', 'Smith', '1985-08-20', 750.25, 'alice@example.com', 'securepass', 'EUR'),
(35252332, 'Daniel', 'Williams', '1988-06-05', 1800.45, 'daniel@example.com', 'pass4321', 'AUD'),
(123456789, 'Tom', 'York', '1999-09-19', 100000, 'parezco_una_salchicha@gmail.com', 'mouse', 'CLP'),
(524215324, 'Michael', 'Johnson', '1982-04-10', 2000.75, 'michael@example.com', 'pass1234', 'GBP');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`IDTransaction`),
  ADD KEY `FK_Transaction_User` (`IDSourceUser`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`IDUser`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `transaction`
--
ALTER TABLE `transaction`
  MODIFY `IDTransaction` int(9) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `FK_Transaction_User` FOREIGN KEY (`IDSourceUser`) REFERENCES `user` (`IDUser`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

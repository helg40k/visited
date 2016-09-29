--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;


SET search_path = visited;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: user; Type: TABLE; Schema: visited; Owner: postgres
--

CREATE TABLE visited.user (
    id bigint PRIMARY KEY NOT NULL,
    email varchar(100) NOT NULL,
    password varchar(100) NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone
);

ALTER TABLE visited.user OWNER TO "visited";
CREATE SEQUENCE visited.user_id_seq START WITH 1;

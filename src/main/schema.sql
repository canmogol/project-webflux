
CREATE OR REPLACE FUNCTION public.notify_event()
    RETURNS trigger
    LANGUAGE plpgsql
as
$function$
DECLARE
    payload JSON;
BEGIN
--     raise warning 'book_notification before row_to_json id: % author: %', NEW.id, NEW.author;
    payload = json_build_object('new', row_to_json(NEW), 'old', row_to_json(OLD), 'operation', TG_OP);
--     raise warning 'book_notification after row_to_json payload: %', payload::text;
    PERFORM pg_notify('book_notification', payload::text);
--     raise warning 'book_notification after pg_notify payload: %', payload::text;
    RETURN NULL;
END;
$function$;

DROP table public.book;

CREATE TABLE IF NOT EXISTS public.book
(
    id     SERIAL PRIMARY KEY,
    title  TEXT NOT NULL,
    author TEXT NOT NULL
);

DROP TRIGGER IF EXISTS notify_book ON public.book;

CREATE TRIGGER notify_book
    AFTER INSERT OR UPDATE OR DELETE
    ON public.book
    FOR EACH ROW
EXECUTE PROCEDURE notify_event();


ALTER TABLE public.book ENABLE ALWAYS TRIGGER notify_book;

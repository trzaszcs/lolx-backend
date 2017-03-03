package pl.poznan.lolx.domain


class ClientException extends Exception {

    ClientException(String msg, Throwable thr) {
        super(msg, thr)
    }

}

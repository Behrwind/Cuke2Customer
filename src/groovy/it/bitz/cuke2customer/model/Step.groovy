package it.bitz.cuke2customer.model

class Step {

    String step
    Table table

    @Override
    String toString () {
        return step
    }
}

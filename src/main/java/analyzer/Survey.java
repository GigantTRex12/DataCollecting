package analyzer;

import dataset.BaseDataSet;

import java.util.List;

class Survey<T extends BaseDataSet> {

    private final List<Question<T>> questions;

    public Survey(List<Question<T>> questions) {
        this.questions = questions;
    }

    void run() {
        // TODO
    }
}

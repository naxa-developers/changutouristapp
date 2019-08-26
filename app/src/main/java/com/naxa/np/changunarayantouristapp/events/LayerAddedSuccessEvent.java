package com.naxa.np.changunarayantouristapp.events;


public class LayerAddedSuccessEvent {

    public static class LayerAddedSuccess{

        private  boolean isAdded;

        public LayerAddedSuccess(boolean isAdded) {
            this.isAdded = isAdded;
        }

        public boolean isAdded() {
            return isAdded;
        }

        public void setAdded(boolean added) {
            isAdded = added;
        }
    }
}

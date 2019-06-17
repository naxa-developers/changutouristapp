package com.naxa.np.changunarayantouristapp.events;


import com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils.MultiItemSectionModel;

public class MapDataLayerListCheckEvent {

    public static class MapDataLayerListCheckedEvent{
        private MultiItemSectionModel multiItemSectionModel;
        private Boolean isChecked;

        public MapDataLayerListCheckedEvent(MultiItemSectionModel multiItemSectionModel, Boolean isChecked) {
            this.multiItemSectionModel = multiItemSectionModel;
            this.isChecked = isChecked;
        }

        public MultiItemSectionModel getMultiItemSectionModel() {
            return multiItemSectionModel;
        }

        public void setMultiItemSectionModel(MultiItemSectionModel multiItemSectionModel) {
            this.multiItemSectionModel = multiItemSectionModel;
        }

        public Boolean getChecked() {
            return isChecked;
        }

        public void setChecked(Boolean checked) {
            isChecked = checked;
        }
    }
}

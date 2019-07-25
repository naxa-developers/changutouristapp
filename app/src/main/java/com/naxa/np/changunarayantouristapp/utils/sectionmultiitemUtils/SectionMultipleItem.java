package com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.SectionMultiEntity;

public class SectionMultipleItem extends SectionMultiEntity<MultiItemSectionModel> implements MultiItemEntity {

    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int IMG_TEXT = 3;
    public static final int MAP_DATA_LIST = 4;
    private int itemType;
    private boolean isMore;
    private boolean isHeadListNo;
    private MultiItemSectionModel multiItemSectionModel;
    private String headerNo;

    public SectionMultipleItem(boolean isHeader, String header, boolean isMore, boolean isHeadListNo, String headerNo) {
        super(isHeader, header);
        this.isMore = isMore;
        this.isHeadListNo = isHeadListNo;
        this.headerNo = headerNo;
    }

    public SectionMultipleItem(int itemType, MultiItemSectionModel multiItemSectionModel) {
        super(multiItemSectionModel);
        this.multiItemSectionModel = multiItemSectionModel;
        this.itemType = itemType;
    }

    public MultiItemSectionModel getMultiItemSectionModel() {
        return multiItemSectionModel;
    }

    public void setMultiItemSectionModel(MultiItemSectionModel multiItemSectionModel) {
        this.multiItemSectionModel = multiItemSectionModel;
    }
    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }


    @Override
    public int getItemType() {
        return itemType;
    }


    public boolean isHeadListNo() {
        return isHeadListNo;
    }

    public void setHeadListNo(boolean headListNo) {
        isHeadListNo = headListNo;
    }

    public String getHeaderNo() {
        return headerNo;
    }

    public void setHeaderNo(String headerNo) {
        this.headerNo = headerNo;
    }
}

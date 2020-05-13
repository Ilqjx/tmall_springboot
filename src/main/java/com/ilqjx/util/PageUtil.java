package com.ilqjx.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageUtil<T> {
    private Page<T> page;
    private int navigatePages;
    private int[] navigatePageNums;

    private int size;
    private int number;
    private int numberOfElements;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private boolean last;
    private boolean hasPrevious;
    private boolean hasNext;
    private boolean hasContent;
    private List<T> content;

    public PageUtil(Page<T> page, int navigatePages) {
        this.page = page;
        this.navigatePages = navigatePages;
        this.size = page.getSize();
        this.number = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.hasContent = page.hasContent();
        this.content = page.getContent();

        calcnavigatePageNums();
    }

    private void calcnavigatePageNums() {
        int[] navigatePageNums;
        if (totalPages < navigatePages) {
            navigatePageNums = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                navigatePageNums[i] = i + 1;
            }
        } else {
            int num = number + 1;
            int startNum = num - navigatePages / 2;
            int endNum = num + navigatePages / 2;
            navigatePageNums = new int[navigatePages];

            if (startNum < 1) {
                startNum = 1;
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNums[i] = startNum++;
                }
            } else if (endNum > totalPages) {
                endNum = totalPages;
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatePageNums[i] = endNum--;
                }
            } else {
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNums[i] = startNum++;
                }
            }
        }
        this.navigatePageNums = navigatePageNums;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int[] getnavigatePageNums() {
        return navigatePageNums;
    }

    public void setnavigatePageNums(int[] navigatePageNums) {
        this.navigatePageNums = navigatePageNums;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasContent() {
        return hasContent;
    }

    public void setHasContent(boolean hasContent) {
        this.hasContent = hasContent;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PageUtil{" +
                "page=" + page +
                ", navigatePages=" + navigatePages +
                ", navigatePageNums=" + Arrays.toString(navigatePageNums) +
                ", size=" + size +
                ", number=" + number +
                ", numberOfElements=" + numberOfElements +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", first=" + first +
                ", last=" + last +
                ", hasPrevious=" + hasPrevious +
                ", hasNext=" + hasNext +
                ", hasContent=" + hasContent +
                ", content=" + content +
                '}';
    }

}

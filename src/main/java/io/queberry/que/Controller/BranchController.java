package io.queberry.que.Controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Slf4j
@AllArgsConstructor
public class BranchController {

    public static <T> Page<T> getPage(Set<T> lists, Pageable pageable) {
        List<T> activeLists = new ArrayList<>(lists);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), activeLists.size());
        List<T> pageContent = activeLists.subList(start, end);
        return new PageImpl<>(pageContent, pageable, activeLists.size());
    }

}


package org.kprsongs.service;

import org.kprsongs.domain.Verse;

import java.util.List;

/**
 * Created by K Purushotham Reddy on 5/9/2015.
 */
public interface IUtilitiesService {

    List<Verse> getVerse(String lyrics);

    List<String> getVerseByVerseOrder(String verseOrder);
}

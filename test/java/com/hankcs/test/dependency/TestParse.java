/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/11/20 18:36</create-date>
 *
 * <copyright file="TestParse.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.test.dependency;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLLoader;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.corpus.dependency.CoNll.Evaluator;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dependency.CRFDependencyParser;
import com.hankcs.hanlp.dependency.MaxEntDependencyParser;
import com.hankcs.hanlp.dependency.WordNatureDependencyParser;
import com.hankcs.hanlp.seg.common.Term;
import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hankcs
 */
public class TestParse extends TestCase
{
    public void testParse() throws Exception
    {
        List<Term> termList = new LinkedList<>();
        termList.add(new Term("坚决", Nature.ad));
        termList.add(new Term("惩治", Nature.v));
        termList.add(new Term("贪污", Nature.v));
        termList.add(new Term("贿赂", Nature.n));
        termList.add(new Term("等", Nature.udeng));
        termList.add(new Term("经济", Nature.n));
        termList.add(new Term("犯罪", Nature.vn));

        System.out.println(MaxEntDependencyParser.compute(termList));
    }

    public void testSegAndParse() throws Exception
    {
//        HanLP.Config.enableDebug();
        System.out.println(WordNatureDependencyParser.compute("欧共体企业在欧洲摄像机市场上所占的份额已由百分之四十八"));
    }

    public void testMaxEntParser() throws Exception
    {
        System.out.println(MaxEntDependencyParser.compute("把市场经济奉行的等价交换原则引入党的生活和国家机关政务活动中"));
    }

    public void testCrfParser() throws Exception
    {
        HanLP.Config.enableDebug();
        List<Term> termList = new LinkedList<>();
        termList.add(new Term("坚决", Nature.ad));
        termList.add(new Term("惩治", Nature.v));
        termList.add(new Term("贪污", Nature.v));
        termList.add(new Term("贿赂", Nature.n));
        termList.add(new Term("等", Nature.udeng));
        termList.add(new Term("经济", Nature.n));
        termList.add(new Term("犯罪", Nature.vn));
        System.out.println(CRFDependencyParser.compute(termList));
    }

    public void testEvaluate() throws Exception
    {
        testParse();
        LinkedList<CoNLLSentence> sentenceList = CoNLLLoader.loadSentenceList("D:\\Doc\\语料库\\依存分析训练数据\\THU\\dev.conll");
        Evaluator evaluator = new Evaluator();
        int id = 1;
        for (CoNLLSentence sentence : sentenceList)
        {
            System.out.printf("%d / %d...", id++, sentenceList.size());
            long start = System.currentTimeMillis();
            List<Term> termList = new LinkedList<>();
            for (CoNLLWord word : sentence.word)
            {
                termList.add(new Term(word.LEMMA, Nature.valueOf(word.POSTAG)));
            }
            CoNLLSentence out = CRFDependencyParser.compute(termList);
            evaluator.e(sentence, out);
            System.out.println("done in " + (System.currentTimeMillis() - start) + " ms.");
        }
        System.out.println(evaluator);
    }
}

package com.mhp.coding.challenges.mapping.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import com.mhp.coding.challenges.mapping.Application;
import com.mhp.coding.challenges.mapping.models.db.Article;
import com.mhp.coding.challenges.mapping.models.db.blocks.ArticleBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.VideoBlockType;
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.ArticleBlockDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.GalleryBlockDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.ImageBlock;
import com.mhp.coding.challenges.mapping.models.dto.blocks.TextBlock;
import com.mhp.coding.challenges.mapping.models.dto.blocks.VideoBlock;
import com.mhp.coding.challenges.mapping.repositories.ArticleRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Anastasiy
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class ArticleMapperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void map_ValidArticle_ReturnsValidArticleDto() {
        Article article = articleRepository.findBy(1001L);
        ArticleDto articleDto = articleMapper.map(article);
        assertThat(articleDto).isInstanceOf(ArticleDto.class);
        assertThat(articleDto.getId()).isEqualTo(article.getId());
        assertThat(articleDto.getTitle()).isEqualTo(article.getTitle());
        assertThat(articleDto.getDescription()).isEqualTo(article.getDescription());
        assertThat(articleDto.getBlocks()).hasSameSizeAs(article.getBlocks());
        articleDto.getBlocks().forEach(block -> {
            if (block instanceof GalleryBlockDto) {
                GalleryBlockDto galleryBlockDto = (GalleryBlockDto) block;
                assertThat(galleryBlockDto.getSortIndex()).isEqualTo(0);
                assertThat(galleryBlockDto.getImages()).hasSize(0);
            }
            if (block instanceof ImageBlock) {
                ImageBlock imageBlockDto = (ImageBlock) block;
                assertThat(imageBlockDto.getSortIndex()).isEqualTo(0);
                assertThat(imageBlockDto.getImage()).isNull();
            }
            if (block instanceof TextBlock) {
                TextBlock textBlockDto = (TextBlock) block;
                assertThat(textBlockDto.getSortIndex()).isIn(1, 3, 4);
                assertThat(textBlockDto.getText()).contains("Text for 1001");
            }
            if (block instanceof VideoBlock) {
                VideoBlock videoBlockDto = (VideoBlock) block;
                assertThat(videoBlockDto.getSortIndex()).isEqualTo(5);
                assertThat(videoBlockDto.getType()).isEqualTo(VideoBlockType.YOUTUBE);
                assertThat(videoBlockDto.getUrl()).isEqualTo("https://youtu.be/myvideo");
            }
        });
    }

    @Test
    public void map_Article_ReturnedDtoContainsSortedBlocks() {
        Article article = articleRepository.findBy(1001L);
        List<Integer> entitySortIndexes = article.getBlocks().stream()
                .map(ArticleBlock::getSortIndex)
                .collect(Collectors.toList());
        List<Integer> entitySortIndexesSorted = new ArrayList<>(entitySortIndexes);
        Collections.sort(entitySortIndexesSorted);
        assertThat(entitySortIndexes).isNotEqualTo(entitySortIndexesSorted);

        ArticleDto articleDto = articleMapper.map(article);
        List<Integer> expectedDtoSortIndexes = Arrays.asList(0, 0, 1, 3, 4, 5);
        List<Integer> actualDtoSortIndexes = articleDto.getBlocks().stream()
                .map(ArticleBlockDto::getSortIndex)
                .collect(Collectors.toList());
        assertThat(actualDtoSortIndexes).isEqualTo(expectedDtoSortIndexes);
    }

    @Test
    public void map_UknownArticleBlockType_ThrowsIllegalStateException() {
        Article article = articleRepository.findBy(1001L);
        ArticleBlock blockMock = mock(ArticleBlock.class);
        article.getBlocks().add(blockMock);

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unknown article block type");
        articleMapper.map(article);
    }

}

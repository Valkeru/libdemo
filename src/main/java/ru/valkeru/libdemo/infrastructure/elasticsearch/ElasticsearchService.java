package ru.valkeru.libdemo.infrastructure.elasticsearch;

import java.io.IOException;
import java.util.Collection;

/**
 * Service to manage Elasticsearch data.<br/>
 * Full data reindexing follows the blue-green indexing strategy:
 * <ul>
 *     <li>Create a new index</li>
 *     <li>Save documents to that index</li>
 *     <li>Refresh the index</li>
 *     <li>Reassign the alias to created index</li>
 * </ul>
 */
public interface ElasticsearchService {

    /**
     * Creates a new physical index to store documents
     * @param clazz {@code @Document} annotated class.
     *              The {@code indexName} attribute value is used as index name prefix
     * @return Created index name
     */
    String createIndex(Class<?> clazz) throws IOException;

    /**
     * Saves all documents to the given index using the Bulk API
     * @param documents Documents to save
     * @param indexName Created index name
     * @param <T> Document type
     */
    <T> void saveAll(Collection<T> documents, String indexName) throws IOException;

    /**
     * Refreshes the specified index to make recently saved documents searchable before reassigning the alias
     *
     * @param indexName Index to refresh
     */
    void refreshIndex(String indexName) throws IOException;

    /**
     * Reassigns the alias defined by the document type to the given index
     * @param clazz Class annotated with {@code @Document}.
     *              The {@code indexName} attribute value is used as alias
     * @param newIndexName Target index name created with {@link #createIndex(Class)}
     */
    void reassignAlias(Class<?> clazz, String newIndexName) throws IOException;
}

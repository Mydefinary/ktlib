import BaseRepository from './BaseRepository';

export default class BookRepository extends BaseRepository {
  constructor(axios) {
    super(axios, 'books');
  }

  /**
   * 책 ID를 기반으로 PDF 생성 및 SAS URL 반환
   * @param {number} bookId
   * @returns {Promise<string>} SAS URL
   */
  async generatePdf(bookId) {
    const url = `/${this.path}/${bookId}/pdf`;
    const response = await this.axios.get(this.fixUrl(url));
    return response.data;  // ✅ SAS URL 반환
  }
}

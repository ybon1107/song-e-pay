const fs = require('fs');
const csv = require('csv-parser');

const csvFilePath = './src/locales/csv/locales.csv'; // CSV 파일 경로
const languages = ['ko', 'en', 'id', 'vi'];
const jsonData = {};

// CSV 파일 읽기
fs.createReadStream(csvFilePath)
  .pipe(csv(csv({
    separator: ',', // 쉼표 구분자로 설정 (기본값)
    quote: '"' // 따옴표로 감싸진 값에 대한 처리
  })))
  .on('data', (row) => {
    // 각 언어에 대한 JSON 데이터 생성
    languages.forEach(lang => {
      if (!jsonData[lang]) {
        jsonData[lang] = {};
      }
      jsonData[lang][row.key] = row[lang]; // 각 언어에 맞는 값 추가
    });
  })
  .on('end', () => {
    // 각 언어에 대한 JSON 파일 생성
    languages.forEach(lang => {
      const jsonFilePath = `./src/locales/json/${lang}.json`;
      fs.writeFileSync(jsonFilePath, JSON.stringify(jsonData[lang], null, 4), 'utf-8');
    });
    console.log("JSON 파일이 성공적으로 생성되었습니다.");
  });

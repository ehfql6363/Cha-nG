// const { jsPDF } = window.jspdf

// const doc = new jsPDF({
//   orientation: 'portrait',
//   unit: 'mm',
//   format: 'a4',
//   hotfixes: ['px_scaling'],
// })

// // 페이지 1을 HTML로 추가
// doc.html(document.querySelector('#page1'), {
//   callback: function (doc) {
//     // 페이지 2를 추가하고 다른 내용을 넣음
//     doc.addPage()
//     doc.html(document.querySelector('#page2'), {
//       callback: function (doc) {
//         // PDF 저장
//         doc.save('document.pdf')
//       },
//     })
//   },
// })

//   const pageWidth = 210
//   const pageHeight = 297
//   //const margin = 10;

//   // Function to generate PDF from HTML elements
//   const generatePdfFromElements = async () => {
//     try {
//       // Generate first page
//       const page1 = await generatePageCanvas('page1')
//       doc.addImage(
//         page1.toDataURL('image/jpeg', 1.0),
//         'JPEG',
//         0,
//         0,
//         pageWidth,
//         pageHeight,
//       )

//       doc.addPage()
//       const page2 = await generatePageCanvas('page2')
//       doc.addImage(
//         page2.toDataURL('image/jpeg', 1.0),
//         'JPEG',
//         0,
//         0,
//         pageWidth,
//         pageHeight,
//       )

//       doc.save('chain_g_서약서.pdf')
//     } catch (error) {
//       console.error('PDF 생성 중 오류 발생:', error)
//       alert('PDF 생성 중 오류가 발생했습니다.')
//     }
//   }

//   const generatePageCanvas = async (pageId) => {
//     const element = document.getElementById(pageId)
//     const canvas = await html2canvas(element, {
//       scale: 2,
//       useCORS: true,
//       logging: false,
//       allowTaint: true,
//     })
//     return canvas
//   }

//   generatePdfFromElements()
// })

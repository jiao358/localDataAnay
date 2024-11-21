import * as XLSX from 'xlsx/xlsx.mjs'
import FileSaver from 'file-saver'

// 导出Excel
export function exportExcel(data, filename = 'export.xlsx') {
  try {
    // 创建工作表
    const ws = XLSX.utils.json_to_sheet(data)

    // 设置列宽
    const colWidth = Object.keys(data[0] || {}).map(() => ({ wch: 20 }))
    ws['!cols'] = colWidth

    // 创建工作簿
    const wb = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1')

    // 生成文件并下载
    const wbout = XLSX.write(wb, { 
      bookType: 'xlsx', 
      type: 'array',
      bookSST: false
    })
    const blob = new Blob([wbout], { 
      type: 'application/octet-stream'
    })
    FileSaver.saveAs(blob, filename)
  } catch (error) {
    console.error('Export Excel failed:', error)
    throw error
  }
}

// 导入Excel
export function importExcel(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = (e) => {
      try {
        const data = e.target.result
        const workbook = XLSX.read(data, { type: 'array' })
        const firstSheetName = workbook.SheetNames[0]
        const worksheet = workbook.Sheets[firstSheetName]
        const results = XLSX.utils.sheet_to_json(worksheet)
        resolve(results)
      } catch (error) {
        reject(error)
      }
    }
    reader.onerror = reject
    reader.readAsArrayBuffer(file)
  })
} 
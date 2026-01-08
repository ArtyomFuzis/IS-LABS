import React, { useState, useEffect } from 'react';
import { bulkImport, fetchData } from '../../util/apiQueries';

// Тип для элемента истории импорта
interface ImportHistoryItem {
  id: number;
  time: number;
  status: string;
  importedObjects: number;
  errorMessage: string | null;
}

function ImportModal({ onClose }: { onClose: () => void }) {
  const [error, setError] = useState<string | null>(null);
  const [errorDetails, setErrorDetails] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);
  const [showDetails, setShowDetails] = useState(false);
  const [history, setHistory] = useState<ImportHistoryItem[]>([]);
  const [loadingHistory, setLoadingHistory] = useState(false);
  const [viewMode, setViewMode] = useState<'import' | 'history'>('import');

  // Функция для загрузки истории импорта
  const loadHistory = async () => {
    setLoadingHistory(true);
    try {
      const response = await fetchData('/operations/bulk/history');
      setHistory(response);
    } catch (err: any) {
      console.error('Ошибка загрузки истории:', err);
    } finally {
      setLoadingHistory(false);
    }
  };

  // При монтировании загружаем историю
  useEffect(() => {
    loadHistory();
  }, []);

  // При успешном импорте обновляем историю
  useEffect(() => {
    if (success) {
      loadHistory();
    }
  }, [success]);

  const handleFileUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    // Проверяем расширение файла
    const fileName = file.name.toLowerCase();
    if (!fileName.endsWith('.yml') && !fileName.endsWith('.yaml')) {
      setError('Неверный формат файла. Разрешены только файлы .yml и .yaml');
      setErrorDetails(null);
      return;
    }

    setLoading(true);
    setError(null);
    setErrorDetails(null);
    setSuccess(false);
    setShowDetails(false);

    try {
      const reader = new FileReader();
      reader.onload = async (e) => {
        try {
          const content = e.target?.result as string;
          const result = await bulkImport(content);
          
          // Обрабатываем ответ сервера
          if (result.success) {
            setSuccess(true);
            // Сбрасываем input файла
            event.target.value = '';
          } else {
            // Анализируем тип ошибки
            if (result.reason) {
              // Сохраняем оригинальный текст ошибки для отображения в подробностях
              const errorText = result.text || '';
              setErrorDetails(errorText);
              
              switch (result.reason) {
                case 'ValidationError':
                case 'YamlSyntaxError':
                  // Для ValidationError и YamlSyntaxError показываем текст ошибки
                  setError(errorText || 'Ошибка валидации данных');
                  break;
                case 'ConstraintViolation':
                  // Для ConstraintViolation показываем общее сообщение
                  setError('Ошибка вторичной валидации');
                  break;
                case 'UnknownException':
                  // Если это UnknownException, но не TransactionRequiredException
                  if (result.text && result.text.includes('TransactionRequiredException')) {
                    setError('Ошибка вторичной валидации');
                  } else {
                    setError('Неизвестная ошибка сервера');
                  }
                  break;
                default:
                  // Для других типов ошибок показываем текст или reason
                  setError(errorText || result.reason || 'Неизвестная ошибка');
              }
            } else {
              setError('Неизвестный формат ошибки');
              setErrorDetails(null);
            }
          }
        } catch (err: any) {
          // Обработка сетевых ошибок или ошибок парсинга
          if (err.response?.data) {
            const data = err.response.data;
            const errorText = data.text || data.message || '';
            setErrorDetails(errorText);
            
            if (data.reason === 'ValidationError' || data.reason === 'YamlSyntaxError') {
              setError(errorText || 'Ошибка обработки файла');
            } else if (data.reason === 'ConstraintViolation') {
              setError('Ошибка вторичной валидации');
            } else if (data.reason?.startsWith('UnknownException')) {
              if (data.text?.includes('TransactionRequiredException')) {
                setError('Ошибка вторичной валидации');
              } else {
                setError('Неизвестная ошибка сервера');
              }
            } else {
              setError(data.message || 'Ошибка импорта файла');
            }
          } else {
            const errorText = err.message || '';
            setErrorDetails(errorText);
            setError(err.message || 'Ошибка импорта файла');
          }
        } finally {
          setLoading(false);
        }
      };
      
      // Читаем файл как текст (для YAML)
      reader.readAsText(file, 'UTF-8');
      
    } catch (err: any) {
      const errorText = err.message || '';
      setErrorDetails(errorText);
      setError(err.message || 'Ошибка чтения файла');
      setLoading(false);
    }
  };

  // Функция для форматирования времени
  const formatTime = (timestamp: number) => {
    const date = new Date(timestamp * 1000);
    return date.toLocaleString('ru-RU', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });
  };

  // Функция для получения статуса в читаемом виде
  const getStatusText = (status: string) => {
    switch (status) {
      case 'SUCCESS': return 'Успех';
      case 'VALIDATION_ERROR': return 'Ошибка валидации';
      case 'SYNTAX_ERROR': return 'Синтаксическая ошибка';
      case 'ERROR': return 'Ошибка';
      default: return status;
    }
  };

  // Функция для получения класса статуса
  const getStatusClass = (status: string) => {
    switch (status) {
      case 'SUCCESS': return 'import-status-success';
      case 'VALIDATION_ERROR': return 'import-status-validation-error';
      case 'SYNTAX_ERROR': return 'import-status-syntax-error';
      case 'ERROR': return 'import-status-error';
      default: return '';
    }
  };

  return (
    <>
      <div className="modal-main-content">
        <div className="modal-header">Импорт данных</div>
        
        {/* Переключение между режимами */}
        <div className="import-tabs" style={{ marginTop: '20px', marginBottom: '20px', display: 'flex', gap: '10px' }}>
          <button 
            className={`menu-button ${viewMode === 'import' ? 'menu-button-chosen' : ''}`}
            onClick={() => setViewMode('import')}
            style={{ padding: '5px 15px' }}
          >
            Импорт файла
          </button>
          <button 
            className={`menu-button ${viewMode === 'history' ? 'menu-button-chosen' : ''}`}
            onClick={() => {
              setViewMode('history');
              loadHistory();
            }}
            style={{ padding: '5px 15px' }}
          >
            История импорта
          </button>
        </div>

        <div className="modal-params-container">
          {viewMode === 'import' ? (
            <>
              {success ? (
                <div className="import-success">
                  <div style={{ color: '#00CC00', fontSize: '24px', marginBottom: '15px' }}>
                    ✓ Импорт успешно завершен
                  </div>
                  <div>Данные успешно импортированы в систему</div>
                </div>
              ) : (
                <>
                  <div style={{ marginBottom: '20px' }}>
                    Выберите YAML файл для импорта. Файл будет отправлен на сервер для обработки.
                    <div style={{ fontSize: '12px', color: '#AAA', marginTop: '5px' }}>
                      Поддерживаемые форматы: .yml, .yaml
                    </div>
                  </div>
                  <div className="modal-parameter">
                    <label className="modal-parameter-label">Файл:</label>
                    <input
                      type="file"
                      accept=".yml,.yaml"
                      onChange={handleFileUpload}
                      disabled={loading}
                      className="modal-parameter-input"
                      style={{ width: '60%', marginRight: '10%' }}
                    />
                  </div>
                </>
              )}
              
              {loading && (
                <div style={{ marginTop: '20px', color: '#FFD700' }}>
                  Загрузка и обработка файла...
                </div>
              )}
              
              {error && (
                <div className="import-error" style={{ marginTop: '20px' }}>
                  <div style={{ color: '#FF5555', marginBottom: '10px' }}>
                    Ошибка импорта
                  </div>
                  <div style={{
                    padding: '10px',
                    background: '#300000',
                    border: '1px solid #993030',
                    borderRadius: '3px',
                    wordBreak: 'break-all',
                    fontSize: '14px',
                    textAlign: 'left',
                    marginBottom: '10px'
                  }}>
                    {error}
                  </div>
                  <button
                    onClick={() => setShowDetails(!showDetails)}
                    style={{
                      background: 'transparent',
                      border: '1px solid #993030',
                      color: '#CEFBCE',
                      padding: '5px 10px',
                      borderRadius: '3px',
                      cursor: 'pointer'
                    }}
                  >
                    {showDetails ? 'Скрыть подробности' : 'Показать подробности'}
                  </button>
                  {showDetails && errorDetails && (
                    <div style={{
                      marginTop: '10px',
                      padding: '10px',
                      background: '#690000',
                      border: '1px solid #993030',
                      borderRadius: '3px',
                      wordBreak: 'break-all',
                      fontSize: '12px',
                      textAlign: 'left',
                      fontFamily: 'monospace',
                      whiteSpace: 'pre-wrap'
                    }}>
                      {errorDetails}
                    </div>
                  )}
                </div>
              )}
            </>
          ) : (
            // Режим истории импорта
            <div className="import-history">
              {loadingHistory ? (
                <div style={{ textAlign: 'center', padding: '20px', color: '#FFD700' }}>
                  Загрузка истории...
                </div>
              ) : history.length === 0 ? (
                <div style={{ textAlign: 'center', padding: '20px', color: '#AAA' }}>
                  История импорта пуста
                </div>
              ) : (
                <div className="history-table-container" style={{overflowY: 'auto' }}>
                  <table className="main-table" style={{ width: '100%', fontSize: '12px' }}>
                    <thead>
                      <tr>
                        <th className="main-table-head-labels">ID</th>
                        <th className="main-table-head-labels">Время</th>
                        <th className="main-table-head-labels">Статус</th>
                        <th className="main-table-head-labels">Импортировано</th>
                        <th className="main-table-head-labels">Сообщение об ошибке</th>
                      </tr>
                    </thead>
                    <tbody>
                      {history.map((item) => (
                        <tr key={item.id} className="main-table-data-row">
                          <td className="main-table-elem">{item.id}</td>
                          <td className="main-table-elem">{formatTime(item.time)}</td>
                          <td className={`main-table-elem ${getStatusClass(item.status)}`}>
                            {getStatusText(item.status)}
                          </td>
                          <td className="main-table-elem">{item.importedObjects}</td>
                          <td className="main-table-elem" style={{ 
                            maxWidth: '200px', 
                            overflow: 'hidden', 
                            textOverflow: 'ellipsis',
                            whiteSpace: 'nowrap' 
                          }} title={item.errorMessage || ''}>
                            {item.errorMessage || '-'}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
      <div className="modal-buttons">
        <button className="modal-close-button" onClick={onClose} disabled={loading}>
          {viewMode === 'import' && success ? 'Закрыть' : 'Отмена'}
        </button>
      </div>
    </>
  );
}

export default ImportModal;
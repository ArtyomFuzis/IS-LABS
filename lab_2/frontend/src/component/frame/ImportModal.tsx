import React, { useState } from 'react';
import { bulkImport } from '../../util/apiQueries';

function ImportModal({ onClose }: { onClose: () => void }) {
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);
  const [showDetails, setShowDetails] = useState(false);

  const handleFileUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const reader = new FileReader();
      reader.onload = async (e) => {
        try {
          const content = e.target?.result as string;
          await bulkImport(content);
          setSuccess(true);
        } catch (err: any) {
          setError(err.response?.data?.message || 'Ошибка импорта файла');
        } finally {
          setLoading(false);
        }
      };
      reader.readAsText(file);
    } catch (err: any) {
      setError(err.message || 'Ошибка чтения файла');
      setLoading(false);
    }
  };

  return (
    <>
      <div className="modal-main-content">
        <div className="modal-header">Импорт данных</div>
        <div className="modal-params-container">
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
                Выберите файл для импорта. Файл будет отправлен на сервер для обработки.
              </div>
              <div className="modal-parameter">
                <label className="modal-parameter-label">Файл:</label>
                <input
                  type="file"
                  accept=".txt,.json,.xml"
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
                Ошибка импорта файла
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
              {showDetails && (
                <div style={{
                  marginTop: '10px',
                  padding: '10px',
                  background: '#690000',
                  border: '1px solid #993030',
                  borderRadius: '3px',
                  wordBreak: 'break-all',
                  fontSize: '14px',
                  textAlign: 'left'
                }}>
                  {error}
                </div>
              )}
            </div>
          )}
        </div>
      </div>
      <div className="modal-buttons">
        <button className="modal-close-button" onClick={onClose} disabled={loading}>
          {success ? 'Закрыть' : 'Отмена'}
        </button>
      </div>
    </>
  );
}

export default ImportModal;
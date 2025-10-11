function ParameterField({required, type}: {required: boolean, type:string}){
    <div className="modal-parameter">
        <div className="modal-parameter-label">Имя
            {required && <span className="modal-parameter-obligatory-mark">*</span>}</div>
        {type == "text" && <input type="text" className="modal-parameter-input"></input>}
    </div>
}

export default ParameterField
import { element, by, ElementFinder } from 'protractor';

export class LeaseComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-lease div table .btn-danger'));
  title = element.all(by.css('jhi-lease div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class LeaseUpdatePage {
  pageTitle = element(by.id('jhi-lease-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  dateSignedInput = element(by.id('field_dateSigned'));
  moveInDateInput = element(by.id('field_moveInDate'));
  noticeOfRemovalOrMoveoutDateInput = element(by.id('field_noticeOfRemovalOrMoveoutDate'));
  endDateInput = element(by.id('field_endDate'));
  amountInput = element(by.id('field_amount'));
  leaseTypeInput = element(by.id('field_leaseType'));
  notesInput = element(by.id('field_notes'));

  newLeaseSelect = element(by.id('field_newLease'));
  personSelect = element(by.id('field_person'));
  vehicleSelect = element(by.id('field_vehicle'));
  petSelect = element(by.id('field_pet'));
  apartmentSelect = element(by.id('field_apartment'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setDateSignedInput(dateSigned: string): Promise<void> {
    await this.dateSignedInput.sendKeys(dateSigned);
  }

  async getDateSignedInput(): Promise<string> {
    return await this.dateSignedInput.getAttribute('value');
  }

  async setMoveInDateInput(moveInDate: string): Promise<void> {
    await this.moveInDateInput.sendKeys(moveInDate);
  }

  async getMoveInDateInput(): Promise<string> {
    return await this.moveInDateInput.getAttribute('value');
  }

  async setNoticeOfRemovalOrMoveoutDateInput(noticeOfRemovalOrMoveoutDate: string): Promise<void> {
    await this.noticeOfRemovalOrMoveoutDateInput.sendKeys(noticeOfRemovalOrMoveoutDate);
  }

  async getNoticeOfRemovalOrMoveoutDateInput(): Promise<string> {
    return await this.noticeOfRemovalOrMoveoutDateInput.getAttribute('value');
  }

  async setEndDateInput(endDate: string): Promise<void> {
    await this.endDateInput.sendKeys(endDate);
  }

  async getEndDateInput(): Promise<string> {
    return await this.endDateInput.getAttribute('value');
  }

  async setAmountInput(amount: string): Promise<void> {
    await this.amountInput.sendKeys(amount);
  }

  async getAmountInput(): Promise<string> {
    return await this.amountInput.getAttribute('value');
  }

  async setLeaseTypeInput(leaseType: string): Promise<void> {
    await this.leaseTypeInput.sendKeys(leaseType);
  }

  async getLeaseTypeInput(): Promise<string> {
    return await this.leaseTypeInput.getAttribute('value');
  }

  async setNotesInput(notes: string): Promise<void> {
    await this.notesInput.sendKeys(notes);
  }

  async getNotesInput(): Promise<string> {
    return await this.notesInput.getAttribute('value');
  }

  async newLeaseSelectLastOption(): Promise<void> {
    await this.newLeaseSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async newLeaseSelectOption(option: string): Promise<void> {
    await this.newLeaseSelect.sendKeys(option);
  }

  getNewLeaseSelect(): ElementFinder {
    return this.newLeaseSelect;
  }

  async getNewLeaseSelectedOption(): Promise<string> {
    return await this.newLeaseSelect.element(by.css('option:checked')).getText();
  }

  async personSelectLastOption(): Promise<void> {
    await this.personSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async personSelectOption(option: string): Promise<void> {
    await this.personSelect.sendKeys(option);
  }

  getPersonSelect(): ElementFinder {
    return this.personSelect;
  }

  async getPersonSelectedOption(): Promise<string> {
    return await this.personSelect.element(by.css('option:checked')).getText();
  }

  async vehicleSelectLastOption(): Promise<void> {
    await this.vehicleSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async vehicleSelectOption(option: string): Promise<void> {
    await this.vehicleSelect.sendKeys(option);
  }

  getVehicleSelect(): ElementFinder {
    return this.vehicleSelect;
  }

  async getVehicleSelectedOption(): Promise<string> {
    return await this.vehicleSelect.element(by.css('option:checked')).getText();
  }

  async petSelectLastOption(): Promise<void> {
    await this.petSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async petSelectOption(option: string): Promise<void> {
    await this.petSelect.sendKeys(option);
  }

  getPetSelect(): ElementFinder {
    return this.petSelect;
  }

  async getPetSelectedOption(): Promise<string> {
    return await this.petSelect.element(by.css('option:checked')).getText();
  }

  async apartmentSelectLastOption(): Promise<void> {
    await this.apartmentSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async apartmentSelectOption(option: string): Promise<void> {
    await this.apartmentSelect.sendKeys(option);
  }

  getApartmentSelect(): ElementFinder {
    return this.apartmentSelect;
  }

  async getApartmentSelectedOption(): Promise<string> {
    return await this.apartmentSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class LeaseDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-lease-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-lease'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
